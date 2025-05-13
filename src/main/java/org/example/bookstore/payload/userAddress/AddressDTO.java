package org.example.bookstore.payload.userAddress;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstore.model.address.District;
import org.example.bookstore.model.address.Province;
import org.example.bookstore.model.address.Ward;

@Getter
@Setter
public class AddressDTO {

    private ProvinceDTO province;
    private DistrictDTO district;
    private WardDTO ward;
    private String detail;
}
