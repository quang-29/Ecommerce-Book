package org.example.bookstore.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private Long storeId;
    private String storeName;
    private BookDTO book;
    private Integer quantity;
    private long productPrice;
    private Integer discountPercent;
    private long discountAmount;
}
