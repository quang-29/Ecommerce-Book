package org.example.bookstore.service;

import org.apache.coyote.BadRequestException;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.enums.Roles;
import org.example.bookstore.model.*;
import org.example.bookstore.payload.request.ChangePasswordRequest;
import org.example.bookstore.payload.request.LoginRequest;
import org.example.bookstore.payload.request.RegisterRequest;
import org.example.bookstore.payload.response.*;
import org.example.bookstore.repository.InvalidTokenRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.security.CurrentUserDetails;
import org.example.bookstore.security.CustomUserDetails;
import org.example.bookstore.security.JwtTokenProvider;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, InvalidTokenRepository invalidTokenRepository, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
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

            String accessToken = jwtTokenProvider.generateToken(userDetail);
            RefreshTokenEntity refreshTokenEntity = refreshTokenService.createRefreshToken(userDetail, userAgent, ipAddress);
            String refreshToken = refreshTokenEntity.getToken();

            return LoginResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .userDetails(userDetail)
                    .build();

        } catch (AuthenticationException ex) {
            return null;
        }
    }

    public void logout(String refreshToken){
        var currentUser = CurrentUserDetails.getCurrentUser();
        if (currentUser != null && refreshToken != null && !refreshToken.isEmpty()){
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
        CartEntity cartEntity = new CartEntity();
        UserEntity user = UserEntity.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(hashPassword)
                .roles(Roles.USER)
                .cartEntity(cartEntity)
                .build();

        UserEntity userSaved = userRepository.save(user);
        cartEntity.setUser(userSaved);
        return true;
    }

    public LoginResponse refreshUser() {
        var user = CurrentUserDetails.getCurrentUser();
        String token = jwtTokenProvider.generateToken(user);
        return LoginResponse.builder()
                .userDetails(user)
                .token(token)
                .build();
    }

    public void changePassword(ChangePasswordRequest request) throws BadRequestException {
        var user = getCurrentUserEntity();
        if(passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            updateUserPasswordAndSaveUser(user, request.getNewPassword());
        } else {
            throw new BadRequestException("Old password is incorrect");
        }
    }

    private UserEntity getCurrentUserEntity(){
        var userDetails = CurrentUserDetails.getCurrentUser();
        return userRepository.findById(Objects.requireNonNull(userDetails)
                .getUserId()).get();
    }

    private void updateUserPasswordAndSaveUser(UserEntity user, String newPassword) throws BadRequestException {
        if(passwordEncoder.matches(newPassword, user.getPassword())){
            throw new BadRequestException("New password cannot be the same as the old password");
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }

}
