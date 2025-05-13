package org.example.bookstore.payload.order;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstore.enums.PaymentGateway;
import org.example.bookstore.enums.PaymentStatus;
import org.example.bookstore.enums.PaymentType;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoDTO {
    private String username;
    private String phoneNumber;
    private String email;
    private UUID userAddress;
    private LocalDate createAt;
    private LocalDate estimatedDeliveryDate;
    private long amount;
    private PaymentGateway gateway;
    private PaymentStatus status;
    private PaymentType type;

}
