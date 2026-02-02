package org.example.bookstore.payload;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDTO {
    private Long cartId;
    private List<CartItemDTO> cartItem = new ArrayList<>();
    private long totalPrice;

}