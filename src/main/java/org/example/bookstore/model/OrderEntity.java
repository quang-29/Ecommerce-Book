package org.example.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    private Date createAt;

    private Date estimatedDeliveryDate;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "payment_id")
    private Long paymentId;

}
