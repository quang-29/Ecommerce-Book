package org.example.bookstore.controller;

import jakarta.validation.Valid;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.PaymentStatus;
import org.example.bookstore.payload.shop.StoreBookCreateRequest;
import org.example.bookstore.payload.shop.StoreBookUpdateRequest;
import org.example.bookstore.service.OrderService;
import org.example.bookstore.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * API cho chủ cửa hàng (SHOP_OWNER) quản lý sách và xem đơn hàng của cửa hàng mình.
 * Admin cũng được phép truy cập mọi cửa hàng.
 */
@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ShopService shopService;
    private final OrderService orderService;

    public ShopController(ShopService shopService, OrderService orderService) {
        this.shopService = shopService;
        this.orderService = orderService;
    }

    // ---- Cửa hàng ----

    @GetMapping("/my-stores")
    @PreAuthorize("@authorizationService.isShopOwner()")
    public ResponseEntity<ServerResponseDto> getMyStores() {
        return ResponseEntity.ok(shopService.getMyStores());
    }

    @PutMapping("/{storeId}/owner/{userId}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> assignOwner(@PathVariable Long storeId, @PathVariable Long userId) {
        return ResponseEntity.ok(shopService.assignOwner(storeId, userId));
    }

    // ---- Quản lý sách của cửa hàng ----

    @GetMapping("/{storeId}/books")
    @PreAuthorize("@authorizationService.canManageStore(#storeId)")
    public ResponseEntity<ServerResponseDto> getBooks(@PathVariable Long storeId,
                                                      @RequestParam(required = false) String keyword,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(required = false) String sortBy,
                                                      @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(shopService.getStoreBooks(storeId, keyword, page, size, sortBy, sortDirection));
    }

    @PostMapping("/{storeId}/books")
    @PreAuthorize("@authorizationService.canManageStore(#storeId)")
    public ResponseEntity<ServerResponseDto> addBook(@PathVariable Long storeId,
                                                     @Valid @RequestBody StoreBookCreateRequest request) {
        return ResponseEntity.ok(shopService.addBookToStore(storeId, request));
    }

    @PutMapping("/{storeId}/books/{storeBookId}")
    @PreAuthorize("@authorizationService.canManageStore(#storeId)")
    public ResponseEntity<ServerResponseDto> updateBook(@PathVariable Long storeId,
                                                        @PathVariable Long storeBookId,
                                                        @Valid @RequestBody StoreBookUpdateRequest request) {
        return ResponseEntity.ok(shopService.updateStoreBook(storeId, storeBookId, request));
    }

    @DeleteMapping("/{storeId}/books/{storeBookId}")
    @PreAuthorize("@authorizationService.canManageStore(#storeId)")
    public ResponseEntity<ServerResponseDto> deactivateBook(@PathVariable Long storeId,
                                                            @PathVariable Long storeBookId) {
        return ResponseEntity.ok(shopService.deactivateStoreBook(storeId, storeBookId));
    }

    // ---- Xem đơn hàng của cửa hàng (chỉ đọc) ----

    @GetMapping("/{storeId}/orders")
    @PreAuthorize("@authorizationService.canManageStore(#storeId)")
    public ResponseEntity<ServerResponseDto> getOrders(@PathVariable Long storeId,
                                                       @RequestParam(required = false) PaymentStatus status,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(required = false) String sortBy,
                                                       @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(orderService.getOrdersByStore(storeId, status, page, size, sortBy, sortDirection));
    }

    @GetMapping("/{storeId}/orders/{orderId}")
    @PreAuthorize("@authorizationService.canManageStore(#storeId)")
    public ResponseEntity<ServerResponseDto> getOrder(@PathVariable Long storeId, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderOfStore(storeId, orderId));
    }
}
