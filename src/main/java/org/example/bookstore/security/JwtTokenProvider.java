package org.example.bookstore.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.enums.Roles;
import org.example.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private final CustomUserDetailsService userDetailsService;
    private Key key;

    public JwtTokenProvider(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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

    public String generateToken(CustomUserDetails userDetail) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + jwtExpirationInMs);
        Map<String, Object> claims = getCustomClaims(userDetail);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetail.getUsername())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(key, SIGNATURE_ALGORITHM)
                .compact();
    }

    private Map<String, Object> getCustomClaims(CustomUserDetails userDetail) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetail.getUserId());
        claims.put("name", userDetail.getName());
        claims.put("role", userDetail.getRoles().name());
        claims.put("phone", userDetail.getPhone());
        claims.put("avatarUrl", userDetail.getAvatarUrl());
        return claims;
    }

    private CustomUserDetails parseToken(String token) {
        Claims claims = parseClaims(token);
        return CustomUserDetails.builder()
                .userId(claims.get("userId", Long.class))
                .name(claims.get("name", String.class))
                .roles(Roles.valueOf(claims.get("role", String.class)))
                .phone(claims.get("phone", String.class))
                .avatarUrl(claims.get("avatarUrl", String.class))
                .build();
    }

    private String parseUserNameFromToken(String token){
        return parseClaims(token).getSubject();
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

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
                ? bearerToken.substring(7) : null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException |  ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        }
        return false;
    }

    public Roles getRolesFromToken(String token){
        try {
            Claims claims = parseClaims(token);
            return Roles.valueOf(claims.get("role", String.class));
        } catch (ExpiredJwtException ex){
            log.error("Expired JWT token", ex.getMessage());
        }
        return null;
    }

    public String getTokenId(String token) {
        String tokenIdStr = parseClaims(token).getId();
        if (tokenIdStr == null) {
            throw new RuntimeException("Token ID is missing from JWT");
        }
        return tokenIdStr;
    }

    public String getUsername(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public Date getExpirationDate(String token) {
        return parseClaims(token).getExpiration();
    }

}
