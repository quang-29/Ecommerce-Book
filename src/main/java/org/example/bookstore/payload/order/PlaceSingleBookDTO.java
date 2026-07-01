package org.example.bookstore.payload.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstore.enums.PaymentType;

import java.util.UUID;

@Getter
@Setter
public class PlaceSingleBookDTO {
    @NotBlank
    private Long storeBookId;
    @NotBlank
    private Long bookId;
    private Long storeId;
    @NotBlank
    private Long addressId;
    @NotBlank
    private PaymentType paymentType;
    private int weight;
    private String voucherCode;
}
