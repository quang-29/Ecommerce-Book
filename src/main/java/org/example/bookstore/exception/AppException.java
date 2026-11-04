package org.example.bookstore.exception;

import lombok.Getter;
import org.example.bookstore.enums.ErrorCode;

@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
