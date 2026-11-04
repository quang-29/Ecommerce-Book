package org.example.bookstore.security;

import org.example.bookstore.enums.MessageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Xác minh Google ID token: chữ ký (JWK của Google), hạn dùng, issuer và audience (client id của app).
 */
@Component
public class GoogleIdTokenVerifier {

    private static final String JWK_SET_URI = "https://www.googleapis.com/oauth2/v3/certs";
    private static final Set<String> ISSUERS = Set.of("https://accounts.google.com", "accounts.google.com");

    private final String clientId;
    private volatile JwtDecoder decoder;

    public GoogleIdTokenVerifier(@Value("${app.google.client-id:}") String clientId) {
        this.clientId = clientId;
    }

    public Jwt verify(String idToken) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalStateException("app.google.client-id is not configured");
        }
        try {
            return decoder().decode(idToken);
        } catch (Exception ex) {
            throw new RuntimeException(MessageException.TOKEN_INVALID.getMessage());
        }
    }

    private JwtDecoder decoder() {
        if (decoder == null) {
            synchronized (this) {
                if (decoder == null) {
                    NimbusJwtDecoder nimbus = NimbusJwtDecoder.withJwkSetUri(JWK_SET_URI).build();
                    OAuth2TokenValidator<Jwt> issuerValidator = jwt ->
                            jwt.getIssuer() != null && ISSUERS.contains(jwt.getIssuer().toString())
                                    ? OAuth2TokenValidatorResult.success()
                                    : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Invalid issuer", null));
                    OAuth2TokenValidator<Jwt> audienceValidator = jwt -> {
                        List<String> aud = jwt.getAudience();
                        return aud != null && aud.contains(clientId)
                                ? OAuth2TokenValidatorResult.success()
                                : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Invalid audience", null));
                    };
                    // JwtValidators.createDefault() đã kiểm tra exp/nbf
                    nimbus.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                            JwtValidators.createDefault(), issuerValidator, audienceValidator));
                    decoder = nimbus;
                }
            }
        }
        return decoder;
    }
}
