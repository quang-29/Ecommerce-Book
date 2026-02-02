package org.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "delivery_type")
public class DeliveryTypeEntity extends BaseEntity {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "name", nullable = false)
    private String deliveryName;


}
