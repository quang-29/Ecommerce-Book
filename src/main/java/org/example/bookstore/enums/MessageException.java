package org.example.bookstore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum MessageException {
    AUTHOR_NOT_FOUND("Author not found"),
    BOOK_NOT_FOUND("Book not found"),
    BOOK_EXIST("Book existed"),
    USER_NOT_FOUND("User not found"),
    UNAUTHORIZED_ACTION("Unauthorized action"),
    CATEGORY_ALREADY_EXISTS("Category already exists"),
    ORDER_NOT_FOUND("Order not found"),
    INVALID_REQUEST("Invalid request"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    UNAUTHENTICATED("Unauthenticated"),
    UNAUTHORIZED("Unauthorized"),
    STORE_NOT_FOUND("Store not found"),
    BOOK_EXISTED("Book existed"),
    TOKEN_INVALID("Token invalid"),
    USER_WITH_EMAIL_EXISTED("User with email existed"),
    USER_WITH_USERNAME_EXISTED("User with username existed"),
    AUTHOR_EXISTED("Author existed"),
    AUTHOR_NOT_EXISTED("Author existed"),
    CATEGORY_NOT_FOUND("Category not found"),
    FILE_UPLOAD_SIZE("Max file size is 5MB"),
    FILE_UPLOAD_EXTENSIONS("Only jpg, png, gif, bmp files are allowed"),
    CART_NOT_FOUND("Cart not found"),
    BOOK_STOCK_PROBLEM("Book quantity is less than requested quantity"),
    CART_NO_FOUND_BOOK("Book is not added to cart"),
    ORDER_ERROR("Cart is empty, add book to the cart to order"),
    PAYMENT_METHOD_NOT_FOUND("Payment method is not existed. Please enter proper payment method!"),
    ROLE_NOT_FOUND("Role not found"),
    FILE_UPLOAD_ERROR("Unable to upload image to s3 bucket"),
    ORDER_CANCELED("Order canceled"),
    ORDER_CANCELED_ERROR("This order cannot be canceled because it is already being processed or completed."),
    REVIEW_NOT_FOUND("Review not found"),
    REVIEW_ALREADY_EXISTS("You already reviewed the book"),
    REVIEW_ERROR_DELETE("You have not permission to delete the review"),
    TOKEN_EXPIRED("Token expired"),
    INVALID_PRICE("Invalid price value"),
    PRICE_MISMATCH("Calculated price does not match provided price"),
    ADDRESS_NOT_FOUND("Address not found"),
    INVALID_ADDRESS("Address ID cannot be null."),
    NOTIFICATION_NOT_FOUND("Notification not found"),
    EMAIL_INVALID("Email address is invalid"),
    VOUCHER_NOT_FOUND("Voucher not found"),
    VOUCHER_INVALID("Voucher is invalid");

    private final String message;


    MessageException(String message) {
        this.message = message;
    }
}
