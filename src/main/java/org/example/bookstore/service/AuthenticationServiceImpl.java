package org.example.bookstore.service;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.*;
import org.example.bookstore.payload.ResetPasswordDTO;
import org.example.bookstore.payload.request.IntrospectRequest;
import org.example.bookstore.payload.request.RefreshTokenRequest;
import org.example.bookstore.payload.request.RegisterRequest;
import org.example.bookstore.payload.response.IntrospectTokenResponse;
import org.example.bookstore.payload.response.LogInResponse;
import org.example.bookstore.payload.response.RefreshTokenResponse;
import org.example.bookstore.repository.InvalidTokenRepository;
import org.example.bookstore.repository.RoleRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.security.JwtTokenProvider;
import org.example.bookstore.service.Interface.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private InvalidTokenRepository invalidTokenRepository;
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public boolean register(RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_WITH_USERNAME_EXISTED);
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_WITH_EMAIL_EXISTED);
        }
        String hashPassword = passwordEncoder.encode(registerRequest.getPassword());
        Cart cart = new Cart();
        Role role = roleRepository.findByRoleName("USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "USER")));
        User user = User.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(hashPassword)
                .roles(Set.of(role))
                .cart(cart)
                .build();

        User userSaved = userRepository.save(user);
        cart.setUser(userSaved);
        return userSaved != null;
    }


    @Override
    public LogInResponse logIn(String email, String password) {
//        User userFindUserByEmail = userRepository.findUserByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("Email", "email", email));
//        boolean isPasswordMatch = passwordEncoder.matches(password, userFindUserByEmail.getPassword());
//        if(!isPasswordMatch) {
//            LogInResponse logInResponse = new LogInResponse();
//            logInResponse.setAuthenticated(false);
//            return logInResponse;
//        }
//        String token = jwtUtils.generateToken(userFindUserByEmail);
        return LogInResponse.builder()
                .authenticated(true)
                .token("token")
                .build();
    }

    @Override
    public IntrospectTokenResponse introspectToken(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isTokenExprired =  invalidTokenRepository.existsByToken(token);
        if(isTokenExprired){
            return IntrospectTokenResponse.builder()
                    .valid(false)
                    .build();
        }
        boolean isTokenValid = jwtTokenProvider.validateToken(token);
        return IntrospectTokenResponse.builder()
                .valid(isTokenValid)
                .build();
    }

    @Override
    public void logOut(HttpServletRequest request) throws ParseException, JOSEException {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new IllegalArgumentException("Token không hợp lệ hoặc không tồn tại!");
        }

        TokenInvalid invalidatedToken = TokenInvalid.builder()
                .id(jwtTokenProvider.getTokenId(token))
                .token(token)
                .expires(jwtTokenProvider.getExpirationDate(token))
                .build();
        invalidTokenRepository.save(invalidatedToken);
    }

    @Override
    @Transactional
    public User saveDeviceToken(String email, String deviceToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setDeviceToken(deviceToken);
        userRepository.save(user);
        return user;
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        String oldRefreshToken = request.getOldRefreshToken();

        if (invalidTokenRepository.existsById(jwtTokenProvider.getTokenId(oldRefreshToken))) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
        TokenPair tokenPair = jwtTokenProvider.refreshToken(oldRefreshToken);

        TokenInvalid tokenInvalid = TokenInvalid.builder()
                .id(jwtTokenProvider.getTokenId(oldRefreshToken))
                .expires(jwtTokenProvider.getExpirationDate(oldRefreshToken))
                .token(oldRefreshToken)
                .build();

        invalidTokenRepository.save(tokenInvalid);

        return RefreshTokenResponse.builder()
                .token(tokenPair.getToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();
    }
    @Override
    public void resetPassword(ResetPasswordDTO dto) {
        boolean isInValid = jwtTokenProvider.validateToken(dto.getToken());
        if(!isInValid)
            throw new AppException(ErrorCode.TOKEN_INVALID);

        User user = jwtTokenProvider.getUser(dto.getToken());
        String hashPwd = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(hashPwd);
        userRepository.save(user);
    }

}
