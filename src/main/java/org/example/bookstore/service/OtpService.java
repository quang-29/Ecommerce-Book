package org.example.bookstore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
public class OtpService {

    private static final String KEY_PREFIX = "login-otp:";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${app.otp.expiration-in-ms}")
    private long otpExpirationInMs;

    public OtpService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateAndStoreOtp(Long userId) {
        String otp = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
        redisTemplate.opsForValue().set(KEY_PREFIX + userId, otp, Duration.ofMillis(otpExpirationInMs));
        return otp;
    }

    public boolean verifyOtp(Long userId, String otp) {
        String key = KEY_PREFIX + userId;
        Object storedOtp = redisTemplate.opsForValue().get(key);
        if (storedOtp == null || otp == null || !storedOtp.toString().equals(otp)) {
            return false;
        }
        redisTemplate.delete(key);
        return true;
    }
}
