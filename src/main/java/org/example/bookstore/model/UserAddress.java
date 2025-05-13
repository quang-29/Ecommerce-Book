package org.example.bookstore.model;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.A;
import org.example.bookstore.model.address.Address;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user_address")
public class UserAddress extends Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String receiverName;

    private String phoneNumber;

    private boolean isPrimary;
}
