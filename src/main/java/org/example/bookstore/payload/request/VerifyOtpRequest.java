package org.example.bookstore.payload.request;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private Long userId;
    private String otp;
}
