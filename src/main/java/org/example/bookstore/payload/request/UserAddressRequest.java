package org.example.bookstore.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressRequest {

    private String username;
    private String receiverName;
    private String phoneNumber;

    @JsonProperty("isPrimary")
    private boolean isPrimary;
    
    private String province;
    private String district;
    private String ward;
    private String detail;


}
