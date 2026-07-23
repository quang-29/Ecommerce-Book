package org.example.bookstore.service;

import org.apache.coyote.BadRequestException;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.enums.Roles;
import org.example.bookstore.model.*;
import org.example.bookstore.payload.request.ChangePasswordRequest;
import org.example.bookstore.payload.request.LoginRequest;
import org.example.bookstore.payload.request.RegisterRequest;
import org.example.bookstore.payload.request.VerifyOtpRequest;
import org.example.bookstore.payload.response.*;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.security.CurrentUserDetails;
import org.example.bookstore.security.CustomUserDetails;
import org.example.bookstore.security.JwtTokenProvider;
import org.example.bookstore.service.cache.UserCacheService;
import org.example.bookstore.service.dto.UserSaveDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final org.example.bookstore.repository.CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserCacheService userCacheService;

    @Value("${app.otp.expiration-in-ms}")
    private long otpExpirationInMs;

    public AuthenticationService(UserRepository userRepository, org.example.bookstore.repository.CartRepository cartRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, OtpService otpService, EmailService emailService, UserCacheService userCacheService) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.otpService = otpService;
        this.emailService = emailService;
        this.userCacheService = userCacheService;
    }

    public LoginResponse login(LoginRequest loginRequest, String userAgent, String ipAddress) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            var userDetail = (CustomUserDetails) authentication.getPrincipal();

            String otp = otpService.generateAndStoreOtp(userDetail.getUserId());
            long otpExpirationInMinutes = otpExpirationInMs / 60000;
            emailService.sendSimpleEmail(
                    userDetail.getEmail(),
                    "Mã xác thực đăng nhập",
                    "Mã OTP đăng nhập của bạn là: " + otp + "\n\n" +
                            "Mã có hiệu lực trong " + otpExpirationInMinutes + " phút. " +
                            "Vui lòng không chia sẻ mã này cho bất kỳ ai."
            );

            return LoginResponse.builder()
                    .otpRequired(true)
                    .userId(userDetail.getUserId())
                    .build();

        } catch (AuthenticationException ex) {
            throw new RuntimeException(MessageException.UNAUTHENTICATED.getMessage());
        }
    }

    public LoginResponse verifyOtp(VerifyOtpRequest request, String userAgent, String ipAddress) {
        if (request.getUserId() == null || !otpService.verifyOtp(request.getUserId(), request.getOtp())) {
            throw new RuntimeException(MessageException.OTP_INVALID.getMessage());
        }

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException(MessageException.USER_NOT_FOUND.getMessage()));
        CustomUserDetails userDetails = toUserDetails(user);

        String accessToken = jwtTokenProvider.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDetails, userAgent, ipAddress);

        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .userDetails(userDetails)
                .build();
    }

    public void logout(String refreshToken) {
        var currentUser = CurrentUserDetails.getCurrentUser();
        if (currentUser != null && refreshToken != null && !refreshToken.isEmpty()) {
            refreshTokenService.revokeRefreshToken(refreshToken);
        }
    }

    public boolean register(RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException(MessageException.USER_WITH_USERNAME_EXISTED.getMessage());
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException(MessageException.USER_WITH_EMAIL_EXISTED.getMessage());
        }
        String hashPassword = passwordEncoder.encode(registerRequest.getPassword());
        UserEntity user = UserEntity.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(hashPassword)
                .roles(Roles.USER)
                .build();
        UserEntity userSaved = userRepository.save(user);
        UserSaveDto userSaveDto =
                UserSaveDto.builder()
                        .id(userSaved.getId().toString())
                        .email(userSaved.getEmail())
                        .userName(userSaved.getUsername())
                        .roles(userSaved.getRoles())
                        .build();
        userCacheService.createUserCatche(userSaveDto);
        CartEntity cartEntity = new CartEntity();
        cartEntity.setUserId(userSaved.getId());
        cartRepository.save(cartEntity);
        return true;
    }

    public LoginResponse refreshUser(String refreshToken, String userAgent, String ipAddress) {
        RefreshTokenEntity oldRefreshToken = refreshTokenService.verifyRefreshToken(refreshToken);
        refreshTokenService.revokeRefreshToken(refreshToken);

        UserEntity user = userRepository.findById(oldRefreshToken.getUserId())
                .orElseThrow(() -> new RuntimeException(MessageException.USER_NOT_FOUND.getMessage()));
        CustomUserDetails userDetails = toUserDetails(user);

        String accessToken = jwtTokenProvider.generateToken(userDetails);
        String newRefreshToken = refreshTokenService.createRefreshToken(userDetails, userAgent, ipAddress);
        return LoginResponse.builder()
                .userDetails(userDetails)
                .token(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void changePassword(ChangePasswordRequest request) throws BadRequestException {
        var user = getCurrentUserEntity();
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            updateUserPasswordAndSaveUser(user, request.getNewPassword());
        } else {
            throw new BadRequestException("Old password is incorrect");
        }
    }

    private UserEntity getCurrentUserEntity() {
        var userDetails = CurrentUserDetails.getCurrentUser();
        return userRepository.findById(Objects.requireNonNull(userDetails)
                .getUserId()).get();
    }

    private void updateUserPasswordAndSaveUser(UserEntity user, String newPassword) throws BadRequestException {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BadRequestException("New password cannot be the same as the old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private CustomUserDetails toUserDetails(UserEntity user) {
        return CustomUserDetails.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getFirstName())
                .phone(user.getPhoneNumber())
                .roles(user.getRoles())
                .password(user.getPassword())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

}
