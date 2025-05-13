package org.example.bookstore.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(700, "User not found", HttpStatus.NOT_FOUND),
    INVALID_REQUEST(701, "Invalid request", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(702, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(703,"Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(704,"Unauthorized",HttpStatus.FORBIDDEN),
    STORE_NOT_FOUND(705,"Store not found",HttpStatus.BAD_REQUEST),
    BOOK_NOT_FOUND(706,"Books not found",HttpStatus.BAD_REQUEST),
    BOOK_EXISTED(707,"Book existed",HttpStatus.BAD_REQUEST),
    AUTHOR_NOT_FOUND(708,"Author not found",HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(709,"Order not found",HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(710,"Token invalid",HttpStatus.BAD_REQUEST),
    USER_WITH_EMAIL_EXISTED(711,"User with email existed",HttpStatus.BAD_REQUEST),
    USER_WITH_USERNAME_EXISTED(712,"User with username existed",HttpStatus.BAD_REQUEST),
    AUTHOR_EXISTED(713,"Author existed",HttpStatus.BAD_REQUEST),
    AUTHOR_NOT_EXISTED(714,"Author existed",HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(715,"Category not found",HttpStatus.BAD_REQUEST),
    CATEGORY_ALREADY_EXISTS(716,"Category already existed",HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_SIZE(717,"Max file size is 5MB",HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_EXTENSIONS(718,"Only jpg, png, gif, bmp files are allowed",HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND(719,"Cart not found",HttpStatus.BAD_REQUEST),
    BOOK_STOCK_PROBLEM(720,"Book quantity is less than requested quantity",HttpStatus.BAD_REQUEST),
    CART_NO_FOUND_BOOK(721,"Book is not added to cart",HttpStatus.BAD_REQUEST),
    ORDER_ERROR(722,"Cart is empty, add book to the cart to order",HttpStatus.BAD_REQUEST),
    PAYMENT_METHOD_NOT_FOUND(722,"Payment method is not existed. Please enter proper payment method!",HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(723,"Role not found",HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_ERROR(724,"Unable to upload image to s3 bucket",HttpStatus.BAD_REQUEST),
    ORDER_CANCELED(725,"Order canceled",HttpStatus.BAD_REQUEST),
    ORDER_CANCELED_ERROR(726,"This order cannot be canceled because it is already being processed or completed.",HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(727,"Review not found",HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACTION(728,"You have not permission to do this action",HttpStatus.BAD_REQUEST),
    REVIEW_ALREADY_EXISTS(729,"You already reviewed the book",HttpStatus.BAD_REQUEST),
    REVIEW_ERROR_DELETE(730,"You have not permission to delete the review",HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(731,"Token expired",HttpStatus.BAD_REQUEST),
    INVALID_PRICE(732, "Invalid price value",HttpStatus.BAD_REQUEST),
    PRICE_MISMATCH(733, "Calculated price does not match provided price",HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND(734, "Address not found",HttpStatus.BAD_REQUEST),
    INVALID_ADDRESS(735, "Address ID cannot be null.",HttpStatus.BAD_REQUEST),
    NOTIFICATION_NOT_FOUND(736, "Notification not found",HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(737, "Email address is invalid",HttpStatus.BAD_REQUEST),

    ;



    private int code;
    private String message;
    private HttpStatus httpStatus;

}