package org.example.bookstore.model.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstore.enums.PaymentGateway;
import org.example.bookstore.enums.PaymentStatus;
import org.example.bookstore.enums.PaymentType;
import jakarta.persistence.Id;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long amount;

    private long feeShip;

    private PaymentGateway gateway;

    private PaymentType type;

    private PaymentStatus status;

    private Date expireAt;

    private Date createdAt = new Date();

    private Date updatedAt;
}
