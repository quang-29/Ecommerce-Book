package org.example.bookstore.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstore.security.CustomUserDetails;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String token;
    private String refreshToken;
    private CustomUserDetails userDetails;
}
