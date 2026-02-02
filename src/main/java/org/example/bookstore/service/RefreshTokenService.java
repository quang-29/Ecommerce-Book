package org.example.bookstore.service;

import jakarta.transaction.Transactional;
import org.example.bookstore.model.RefreshTokenEntity;
import org.example.bookstore.repository.RefreshTokenRepository;
import org.example.bookstore.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // generate refreshToken using userAgent and ipAddress
    @Transactional
    public RefreshTokenEntity createRefreshToken(CustomUserDetails customUserDetails, String userAgent, String ipAddress){

        String token = generateRefreshToken();
        LocalDateTime expiredDate = LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000);
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(token)
                .userId(customUserDetails.getUserId())
                .expiredDate(expiredDate)
                .revoked(false)
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .build();
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    private String generateRefreshToken() {
        byte[] randomBytes = new byte[64];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Transactional
    public void revokeRefreshToken(String token){
        refreshTokenRepository.revokeToken(token);
    }
}
