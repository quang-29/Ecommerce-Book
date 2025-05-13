package org.example.bookstore.model;

import lombok.Getter;
import lombok.Setter;
import org.example.bookstore.model.address.Address;
import org.example.bookstore.model.address.District;
import org.example.bookstore.model.address.Province;
import org.example.bookstore.model.address.Ward;

@Getter
@Setter
public class ShopAddress extends Address {
    public ShopAddress() {
        this.province = new Province(201, "Hà Nội");
        this.district = new District(1710, 201, "Thanh Trì");
        this.ward = new Ward("1A1111",1710, "Tân Triều");
        this.detail = "144 Chiến Thắng";
    }
}
