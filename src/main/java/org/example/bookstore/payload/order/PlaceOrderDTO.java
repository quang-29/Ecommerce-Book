package org.example.bookstore.payload.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstore.enums.PaymentType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class PlaceOrderDTO {

    @NotBlank
    private Long cartId;

    @NotBlank
    private String shippingAddress;

    @NotBlank
    private PaymentType paymentType;

    private int weight;

    private Map<Long, String> voucherCodes;

}
