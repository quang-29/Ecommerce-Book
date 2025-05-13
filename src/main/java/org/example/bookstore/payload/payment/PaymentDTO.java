package org.example.bookstore.payload.payment;

import lombok.Getter;
import lombok.Setter;
import org.example.bookstore.enums.PaymentGateway;
import org.example.bookstore.enums.PaymentStatus;
import org.example.bookstore.enums.PaymentType;

import java.util.Date;

@Getter
@Setter
public class PaymentDTO {

    private long amount;
    private long feeShip;
    private PaymentGateway gateway;
    private PaymentType type;
    private PaymentStatus status;
    private Date createdAt = new Date();
}
