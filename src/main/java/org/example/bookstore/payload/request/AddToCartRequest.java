package org.example.bookstore.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequest {
    private Long cartId;
    private Long bookId;
    private int quantity;
}
