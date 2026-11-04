package org.example.bookstore.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateStoreVoucherRequest {
    private Long storeId;
    private String code;
    private Integer discountPercent;
    private Long discountAmount;
    private Long minOrderAmount;
    private boolean active = true;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
