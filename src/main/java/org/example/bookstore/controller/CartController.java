package org.example.bookstore.controller;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.request.AddToCartRequest;
import org.example.bookstore.service.Interface.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/getCartByUserName/{userName}")
    @PreAuthorize("#userName == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> getCartByUserId(@PathVariable String userName) {
        return ResponseEntity.ok(ServerResponseDto.success(cartService.getCartByUserName(userName)));
    }

    @PostMapping("/addBookToCart")
    public ResponseEntity<ServerResponseDto> addBookToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(ServerResponseDto.success(cartService.addProductToCart(request.getCartId(), request.getBookId(), request.getQuantity())));
    }

    @DeleteMapping("/deleteBookFromCart")
    public ResponseEntity<ServerResponseDto> deleteBookFromCart(@RequestParam UUID cartId,
                                                                @RequestParam UUID bookId) {
        return ResponseEntity.ok(ServerResponseDto.success(cartService.deleteProductFromCart(cartId, bookId)));
    }

    @PostMapping("/decreaseBookFromCart")
    public ResponseEntity<ServerResponseDto> decreaseBookFromCart(@RequestParam UUID cartId,
                                                                  @RequestParam UUID bookId) {
        return ResponseEntity.ok(ServerResponseDto.success(cartService.decreaseProductFromCart(cartId, bookId)));

    }
}
