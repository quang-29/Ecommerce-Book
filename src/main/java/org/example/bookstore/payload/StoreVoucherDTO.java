package org.example.bookstore.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StoreVoucherDTO {
    private Long id;
    private Long storeId;
    private String storeName;
    private String code;
    private Integer discountPercent;
    private Long discountAmount;
    private Long minOrderAmount;
    private boolean active;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
