package org.example.bookstore.payload.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlaceSingleBookDTO {
    @NotBlank
    private UUID bookId;
    @NotBlank
    private UUID addressId;
    @NotBlank
    private String paymentType;
    private int weight;
}
