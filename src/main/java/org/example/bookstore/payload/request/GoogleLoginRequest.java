package org.example.bookstore.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequest {
    // ID token (JWT) do Google Identity Services trả về cho frontend
    @NotBlank
    private String idToken;
}
