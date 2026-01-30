package org.example.bookstore.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.payload.ResetPasswordDTO;
import org.example.bookstore.payload.request.*;
import org.example.bookstore.payload.response.*;
import org.example.bookstore.security.JwtTokenProvider;
import org.example.bookstore.service.Interface.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final String SIGN_UP_SUCCESS = "Sign Up Successful";
    private static final String SIGN_UP_FAILED = "Sign Up Failed";
    private static final String LOGIN_SUCCESS = "Login Successful";
    private static final String LOGIN_FAILED = "Login Failed";

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtTokenProvider.createToken(authentication);
            String refreshedToken = jwtTokenProvider.createRefreshToken(authentication);

            DataResponse dataResponse = DataResponse.builder()
                    .code(HttpStatus.OK.value())
                    .message("Đăng nhập thành công")
                    .data(new AuthenticationResponse(accessToken, refreshedToken))
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(dataResponse);

        } catch (AuthenticationException ex) {
            DataResponse errorResponse = DataResponse.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Tên đăng nhập hoặc mật khẩu không đúng")
                    .status(HttpStatus.UNAUTHORIZED)
                    .timestamp(LocalDateTime.now())
                    .build();

            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<DataResponse> register(@RequestBody RegisterRequest registerRequest) {
        boolean isSuccess = authenticationService.register(registerRequest);
        String message = isSuccess ? SIGN_UP_SUCCESS : SIGN_UP_FAILED;
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .data(isSuccess)
                .message(message)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/introspectToken")
    public ResponseEntity<IntrospectTokenResponse> introspect(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        IntrospectTokenResponse response = authenticationService.introspectToken(introspectRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logOut")
    public ResponseEntity<DataResponse> refreshToken(HttpServletRequest request) throws ParseException, JOSEException, ParseException, JOSEException {
        authenticationService.logOut(request);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Dang xuat thanh cong")
                .data("")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(dataResponse);
    }

//    @PostMapping("/oauth2/login")
//    public ResponseEntity<?> oauth2Login(@RequestParam(value = "provider") String provider,
//                                         @RequestParam(value = "code") String code){
//        AuthResponseDTO res = authFacadeService.oauthLogin(code, provider);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, getRefreshTokenCookie(res.getRefreshToken()).toString())
//                .body(Map.of(
//                        "token", res.getAccessToken()
//                ));
//    }

    @GetMapping("/user")
    public String user(Principal principal) {
//        System.out.println(principal.getName());
        return "principal";
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<DataResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(DataResponse.builder()
                .data(refreshTokenResponse)
                .timestamp(LocalDateTime.now())
                .message("Refresh Token Successfully!")
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ResetPasswordDTO dto){
        authenticationService.resetPassword(dto);
        return ResponseEntity.ok(Map.of(
                "status", "success"
        ));
    }
}