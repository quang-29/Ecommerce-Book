package org.example.bookstore.service;

import jakarta.transaction.Transactional;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.model.RefreshTokenEntity;
import org.example.bookstore.repository.RefreshTokenRepository;
import org.example.bookstore.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-expiration-in-ms}")
    private Long refreshTokenExpiration;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // generate refreshToken using userAgent and ipAddress
    @Transactional
    public String createRefreshToken(CustomUserDetails customUserDetails, String userAgent, String ipAddress){

        String rawToken = generateRefreshToken();
        LocalDateTime expiredDate = LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000);
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(hashToken(rawToken))
                .userId(customUserDetails.getUserId())
                .expiredDate(expiredDate)
                .revoked(false)
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return rawToken;
    }

    @Transactional
    public RefreshTokenEntity verifyRefreshToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new RuntimeException(MessageException.TOKEN_INVALID.getMessage());
        }

        RefreshTokenEntity refreshToken = refreshTokenRepository.findActiveToken(hashToken(rawToken))
                .orElseThrow(() -> new RuntimeException(MessageException.TOKEN_INVALID.getMessage()));

        if (refreshToken.getExpiredDate() == null || refreshToken.getExpiredDate().isBefore(LocalDateTime.now())) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new RuntimeException(MessageException.TOKEN_EXPIRED.getMessage());
        }

        return refreshToken;
    }

    private String generateRefreshToken() {
        byte[] randomBytes = new byte[64];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Transactional
    public void revokeRefreshToken(String rawToken){
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }
        refreshTokenRepository.revokeToken(hashToken(rawToken));
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
