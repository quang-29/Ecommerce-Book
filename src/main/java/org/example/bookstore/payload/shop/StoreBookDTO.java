package org.example.bookstore.payload.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreBookDTO {
    private Long storeBookId;
    private Long storeId;
    private Long bookId;
    private String title;
    private String isbn;
    private String imagePath;
    private long basePrice;
    private Integer discountPercent;
    private long effectivePrice;
    private Long stock;
    private boolean active;
}
