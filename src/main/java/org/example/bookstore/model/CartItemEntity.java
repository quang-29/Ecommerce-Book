package org.example.bookstore.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItemEntity extends BaseEntity {

    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "store_book_id")
    private Long storeBookId;

    private Integer quantity;

    private long bookPrice;

}
