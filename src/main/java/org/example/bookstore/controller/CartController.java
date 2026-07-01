package org.example.bookstore.controller;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.request.AddToCartRequest;
import org.example.bookstore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.Long;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/userid/{userId}")
    @PreAuthorize("@authorizationService.isAdmin() || @authorizationService.isMySelf(#userId)")
    public ResponseEntity<ServerResponseDto> getCartByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(ServerResponseDto.success(cartService.getCartByUserId(userId)));
    }

    @PostMapping("/addBookToCart")
    public ResponseEntity<ServerResponseDto> addBookToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(ServerResponseDto.success(cartService.addProductToCart(request.getCartId(), request.getStoreBookId(), request.getBookId(), request.getStoreId(), request.getQuantity())));
    }

    @DeleteMapping("/deleteBookFromCart")
    public ResponseEntity<ServerResponseDto> deleteBookFromCart(@RequestParam Long cartId,
                                                            @RequestParam(required = false) Long storeBookId,
                                                            @RequestParam(required = false) Long bookId,
                                                            @RequestParam(required = false) Long storeId) {
        return ResponseEntity.ok(ServerResponseDto.success(cartService.deleteProductFromCart(cartId, storeBookId, bookId, storeId)));
    }

    @PostMapping("/decreaseBookFromCart")
    public ResponseEntity<ServerResponseDto> decreaseBookFromCart(@RequestParam Long cartId,
                                                              @RequestParam(required = false) Long storeBookId,
                                                              @RequestParam(required = false) Long bookId,
                                                              @RequestParam(required = false) Long storeId) {
        return ResponseEntity.ok(ServerResponseDto.success(cartService.decreaseProductFromCart(cartId, storeBookId, bookId, storeId)));

    }
}
