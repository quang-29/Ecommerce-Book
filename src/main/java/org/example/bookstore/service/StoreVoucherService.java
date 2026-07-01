package org.example.bookstore.service;

import jakarta.transaction.Transactional;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.StoreEntity;
import org.example.bookstore.model.StoreVoucherEntity;
import org.example.bookstore.payload.StoreVoucherDTO;
import org.example.bookstore.payload.request.CreateStoreVoucherRequest;
import org.example.bookstore.repository.StoreRepository;
import org.example.bookstore.repository.StoreVoucherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreVoucherService {
    private final StoreVoucherRepository storeVoucherRepository;
    private final StoreRepository storeRepository;

    public StoreVoucherService(StoreVoucherRepository storeVoucherRepository, StoreRepository storeRepository) {
        this.storeVoucherRepository = storeVoucherRepository;
        this.storeRepository = storeRepository;
    }

    @Transactional
    public ServerResponseDto createVoucher(CreateStoreVoucherRequest request) {
        StoreEntity storeEntity = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.STORE_NOT_FOUND));

        StoreVoucherEntity voucher = new StoreVoucherEntity();
        voucher.setStoreEntity(storeEntity);
        voucher.setVoucherCode(request.getCode());
        voucher.setDiscountPercent(clampPercent(request.getDiscountPercent()));
        voucher.setDiscountAmount(nonNegative(request.getDiscountAmount()));
        voucher.setMinOrderAmount(nonNegative(request.getMinOrderAmount()));
        voucher.setActive(request.isActive());
        voucher.setStartAt(request.getStartAt());
        voucher.setEndAt(request.getEndAt());
        return ServerResponseDto.success(toDto(storeVoucherRepository.save(voucher)));
    }

    public ServerResponseDto getVouchersByStore(Long storeId) {
        List<StoreVoucherDTO> vouchers = storeVoucherRepository.findByStoreEntityId(storeId).stream()
                .map(this::toDto)
                .toList();
        return ServerResponseDto.success(vouchers);
    }

    private StoreVoucherDTO toDto(StoreVoucherEntity voucher) {
        StoreVoucherDTO dto = new StoreVoucherDTO();
        dto.setId(voucher.getId());
        dto.setStoreId(voucher.getStoreEntity().getId());
        dto.setStoreName(voucher.getStoreEntity().getName());
        dto.setCode(voucher.getVoucherCode());
        dto.setDiscountPercent(voucher.getDiscountPercent());
        dto.setDiscountAmount(voucher.getDiscountAmount());
        dto.setMinOrderAmount(voucher.getMinOrderAmount());
        dto.setActive(voucher.isActive());
        dto.setStartAt(voucher.getStartAt());
        dto.setEndAt(voucher.getEndAt());
        return dto;
    }

    private int clampPercent(Integer value) {
        if (value == null) {
            return 0;
        }
        return Math.max(0, Math.min(100, value));
    }

    private long nonNegative(Long value) {
        return value == null ? 0L : Math.max(0L, value);
    }
}
