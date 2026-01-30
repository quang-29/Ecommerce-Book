package org.example.bookstore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MessageException {
    AUTHOR_NOT_FOUND("Author not found"),
    BOOK_NOT_FOUND("Book not found"),
    BOOK_EXIST("Book existed"),
    USER_NOT_FOUND("User not found"),
    UNAUTHORIZED_ACTION("Unauthorized action");


    private String message;

}
