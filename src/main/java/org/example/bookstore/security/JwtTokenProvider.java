package org.example.bookstore.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.TokenPair;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-in-ms}")
    private int jwtExpirationInMs;

    @Value("${app.jwt.refresh-expiration-in-ms}")
    private int refreshExpirationInMs;

    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private Key key;

    public JwtTokenProvider(CustomUserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createToken(Authentication authentication) {
        return buildToken(authentication, jwtExpirationInMs, false);
    }

    public String createRefreshToken(Authentication authentication) {
        return buildToken(authentication, refreshExpirationInMs, true);
    }

    private String buildToken(Authentication authentication, int expirationTime, boolean isRefresh) {
        String username = authentication.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        UUID tokenId = UUID.randomUUID();

        JwtBuilder builder = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", tokenId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT");

        if (isRefresh) {
            builder.claim("type", "refresh");
        } else {
            String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            builder.claim("scope", authorities);
        }

        return builder.compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
                ? bearerToken.substring(7) : null;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            String type = claims.get("type", String.class);
            if ("refresh".equals(type)) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException | SecurityException e) {
            log.error("JWT error: {}", e.getMessage());
        }
        return false;
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public User getUser(String token) {
        String username = getUsername(token);
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public UUID getTokenId(String token) {
        String tokenIdStr = parseClaims(token).get("id", String.class);
        if (tokenIdStr == null) {
            throw new RuntimeException("Token ID is missing from JWT");
        }
        return UUID.fromString(tokenIdStr);
    }

    public Date getExpirationDate(String token) {
        return parseClaims(token).getExpiration();
    }

    public TokenPair refreshToken(String oldRefreshToken) {
        try {
            Claims claims = parseClaims(oldRefreshToken);
            String username = claims.getSubject();
            String tokenType = claims.get("type", String.class);

            if (!"refresh".equals(tokenType)) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            String newAccessToken = createToken(authentication);
            String newRefreshToken = createRefreshToken(authentication);

            return new TokenPair(newAccessToken, newRefreshToken);

        } catch (ExpiredJwtException e) {
            log.error("Refresh token expired: {}", e.getMessage());
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            log.error("Invalid token: {}", e.getMessage());
            throw new AppException(ErrorCode.TOKEN_INVALID);
        } catch (Exception e) {
            log.error("Token refresh error: {}", e.getMessage());
            throw new RuntimeException("Cannot refresh token.");
        }
    }


    private String buildScope(User user) {
        if (CollectionUtils.isEmpty(user.getRoles())) return "";
        return user.getRoles().stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.joining(" "));
    }
}
