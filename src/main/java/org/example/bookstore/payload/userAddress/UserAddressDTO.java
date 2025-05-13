package org.example.bookstore.payload.userAddress;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddressDTO extends AddressDTO {
    private String receiverName;

    private String phoneNumber;

    private boolean isPrimary;
}
