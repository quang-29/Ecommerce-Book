package org.example.bookstore.payload.response;

import lombok.*;
import org.example.bookstore.model.TokenPair;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenResponse {
    private String token;
    private String refreshToken;
}
