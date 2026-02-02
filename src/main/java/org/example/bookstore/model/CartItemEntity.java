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

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cartEntity;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity bookEntity;

    private Integer quantity;

    private long bookPrice;

}
