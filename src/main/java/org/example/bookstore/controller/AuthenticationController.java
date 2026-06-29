package org.example.bookstore.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.request.*;
import org.example.bookstore.payload.response.*;
import org.example.bookstore.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private final static String USER_REFRESH_TOKEN_COOKIE = "user_refresh_token";

    @Value("${app.jwt.refresh-expiration-in-ms}")
    private Long refreshTokenExpiration;

    public final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response ) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(request);
        LoginResponse loginResponse = authenticationService.login(loginRequest, userAgent, ipAddress);
        setRefreshTokenCookie(response, loginResponse.getRefreshToken(), USER_REFRESH_TOKEN_COOKIE);
        loginResponse.setRefreshToken(null);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ServerResponseDto> logout(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = getRefreshToken(request,USER_REFRESH_TOKEN_COOKIE);
        authenticationService.logout(refreshToken);
        clearRefreshTokenCookie(response,USER_REFRESH_TOKEN_COOKIE);
        return ResponseEntity.ok(ServerResponseDto.SUCCESS);
    }

    @PostMapping("/register")
    public ResponseEntity<ServerResponseDto> register(@RequestBody RegisterRequest registerRequest) {
        boolean isSuccess = authenticationService.register(registerRequest);
        return ResponseEntity.ok(ServerResponseDto.success(isSuccess));
    }

    @PostMapping("/refresh-user")
    public ResponseEntity<ServerResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshToken(request, USER_REFRESH_TOKEN_COOKIE);
        LoginResponse loginResponse = authenticationService.refreshUser(
                refreshToken,
                request.getHeader("User-Agent"),
                getClientIpAddress(request)
        );
        setRefreshTokenCookie(response, loginResponse.getRefreshToken(), USER_REFRESH_TOKEN_COOKIE);
        loginResponse.setRefreshToken(null);
        return ResponseEntity.ok(ServerResponseDto.success(loginResponse));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ServerResponseDto> changePassword(@RequestBody ChangePasswordRequest request) throws BadRequestException {
        authenticationService.changePassword(request);
        return ResponseEntity.ok(ServerResponseDto.SUCCESS);
    }

    private String getRefreshToken(HttpServletRequest request, String cookieName) {
        var cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie: cookies){
                if(cookieName.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void clearRefreshTokenCookie(HttpServletResponse response, String cookieName){
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, String cookieName) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            clearRefreshTokenCookie(response, cookieName);
            return;
        }
        ResponseCookie cookie = ResponseCookie.from(cookieName, refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMillis(refreshTokenExpiration))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
