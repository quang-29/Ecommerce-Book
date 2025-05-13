package org.example.bookstore.service.Interface;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.bookstore.model.User;
import org.example.bookstore.payload.ResetPasswordDTO;
import org.example.bookstore.payload.request.IntrospectRequest;
import org.example.bookstore.payload.request.RefreshTokenRequest;
import org.example.bookstore.payload.request.RegisterRequest;
import org.example.bookstore.payload.response.IntrospectTokenResponse;
import org.example.bookstore.payload.response.LogInResponse;
import org.example.bookstore.payload.response.RefreshTokenResponse;

import java.text.ParseException;


public interface AuthenticationService {
    boolean register(RegisterRequest registerRequest);
    LogInResponse logIn(String email, String password);
    IntrospectTokenResponse introspectToken(IntrospectRequest request) throws ParseException, JOSEException;
    void logOut(HttpServletRequest request) throws ParseException, JOSEException;

    User saveDeviceToken(String email, String deviceToken) throws  Exception;
    RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
    void resetPassword(ResetPasswordDTO dto);
}
