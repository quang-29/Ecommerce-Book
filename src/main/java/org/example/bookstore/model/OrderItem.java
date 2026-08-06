package org.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "store_book_id")
    private Long storeBookId;

    @Column(name = "order_id")
    private Long orderId;

    private Integer quantity;

    private long productPrice;

    private Integer discountPercent = 0;

    private long discountAmount = 0;

}
