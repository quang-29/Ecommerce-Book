package org.example.bookstore.exception;

import org.example.bookstore.enums.MessageException;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;

    public ResourceNotFoundException() {
        this.message = null;
    }

    public ResourceNotFoundException(MessageException messageException) {
        this.message = messageException.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
