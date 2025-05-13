package org.example.bookstore.controller;

import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.CartDTO;
import org.example.bookstore.payload.CartItemDTO;
import org.example.bookstore.payload.request.AddToCartRequest;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.service.Interface.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private static final String DELETE_BOOK_SUCCESS = "Delete book from cart successfully";
    private static final String DELETE_BOOK_FAILED = "Delete book from cart failed";


    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/getCartByUserName/{userName}")
    @PreAuthorize("#userName == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<DataResponse> getCartByUserId(@PathVariable String userName) {
        CartDTO cartDTO = cartService.getCartByUserName(userName);
        DataResponse response = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .data(cartDTO)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/addBookToCart")
    public ResponseEntity<DataResponse> addBookToCart(@RequestBody AddToCartRequest request) {
        CartDTO cartDTO = cartService.addProductToCart(request.getCartId(), request.getBookId(), request.getQuantity());
        DataResponse response = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .data(cartDTO)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteBookFromCart")
    public ResponseEntity<DataResponse> deleteBookFromCart(@RequestParam UUID cartId,
                                                           @RequestParam UUID bookId) {
        boolean isDeleteSuccess = cartService.deleteProductFromCart(cartId, bookId);
        DataResponse dataResponse = DataResponse.builder()
                .data(isDeleteSuccess)
                .code(HttpStatus.OK.value())
                .message(isDeleteSuccess?DELETE_BOOK_SUCCESS:DELETE_BOOK_FAILED)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/decreaseBookFromCart")
    public ResponseEntity<DataResponse> decreaseBookFromCart(@RequestParam UUID cartId,
                                                             @RequestParam UUID bookId
    ) {
        CartDTO cartDTO = cartService.decreaseProductFromCart(cartId, bookId);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .data(cartDTO)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);

    }
}
