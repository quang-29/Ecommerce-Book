package org.example.bookstore.exception;

import com.google.api.gax.rpc.NotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.payload.response.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerResponseDto> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ServerResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ServerResponseDto> handleNotFoundException(NotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ServerResponseDto.error(ex.getMessage()));

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ServerResponseDto> handleRunTimeException(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ServerResponseDto.error(ex.getMessage()));

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ServerResponseDto> handleRunTimeException(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ServerResponseDto.error(ex.getMessage()));

    }

//    @ExceptionHandler(value = FileUploadException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<DataResponse> handleBadCredentialsException(FileUploadException fileUploadException) {
//        ErrorCode errorCode = fileUploadException.getErr
//        DataResponse dataResponse = DataResponse.builder()
//                .code(HttpStatus.BAD_REQUEST.value())
//                .message(errorCode.getMessage())
//                .status(HttpStatus.BAD_REQUEST)
//                .data("")
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dataResponse);
//    }

//    // Xử lý lỗi xác thực chung
//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Map<String, String> handleAuthenticationException(AuthenticationException ex) {
//        Map<String, String> response = new HashMap<>();
//        response.put("error", "Unauthorized");
//        response.put("message", "Xác thực không thành công!");
//        return response;
//    }
}
