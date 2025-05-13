package org.example.bookstore.model.shipment;


import lombok.*;
import org.example.bookstore.model.address.Address;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentInfo {

    private Address from;

    private Address to;

    private int weight ;

}
