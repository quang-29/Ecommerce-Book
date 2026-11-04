package org.example.bookstore.controller;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.request.CreateStoreVoucherRequest;
import org.example.bookstore.service.StoreVoucherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/store-voucher")
public class StoreVoucherController {
    private final StoreVoucherService storeVoucherService;

    public StoreVoucherController(StoreVoucherService storeVoucherService) {
        this.storeVoucherService = storeVoucherService;
    }

    @PostMapping("/add")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> createVoucher(@RequestBody CreateStoreVoucherRequest request) {
        return ResponseEntity.ok(storeVoucherService.createVoucher(request));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ServerResponseDto> getVouchersByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeVoucherService.getVouchersByStore(storeId));
    }
}
