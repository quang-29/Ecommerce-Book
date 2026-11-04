package org.example.bookstore.payload.shop;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreBookCreateRequest {
    @NotNull
    private Long bookId;

    // Giá riêng của cửa hàng; null thì dùng giá gốc của sách
    @Min(0)
    private Long price;

    @NotNull
    @Min(0)
    private Long stock;
}
