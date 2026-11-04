package org.example.bookstore.payload.shop;

import jakarta.validation.constraints.Min;
import lombok.Data;

// Trường nào null thì giữ nguyên
@Data
public class StoreBookUpdateRequest {
    @Min(0)
    private Long price;

    @Min(0)
    private Long stock;

    private Boolean active;
}
