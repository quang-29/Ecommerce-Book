package org.example.bookstore.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlaceOrderResponse {

    private UUID orderId;

    private String paymentUrl;

}