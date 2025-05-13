package org.example.bookstore.controller;

import org.example.bookstore.model.UserAddress;
import org.example.bookstore.model.address.Address;
import org.example.bookstore.model.shipment.BasicShippingOrderInfo;
import org.example.bookstore.model.shipment.ShipmentInfo;
import org.example.bookstore.payload.request.UserAddressRequest;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.payload.userAddress.UserAddressDTO;
import org.example.bookstore.service.Interface.UserAddressService;
import org.example.bookstore.service.shipment.GHNService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class UserAddressController {
    private final UserAddressService userAddressService;

    private final GHNService ghnService;

    public UserAddressController(UserAddressService userAddressService, GHNService ghnService) {
        this.userAddressService = userAddressService;
        this.ghnService = ghnService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<DataResponse> getUsername(@PathVariable String username) {

        List<UserAddress> userAddressList = userAddressService.getAddressListByUser(username);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .data(userAddressList)
                .timestamp(LocalDateTime.now())
                .message("Success")
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/addUserAddress")
    public ResponseEntity<DataResponse> addUserAddress(@RequestBody UserAddressRequest userAddressRequest) {
         userAddressService.save(userAddressRequest);
         DataResponse dataResponse = DataResponse.builder()
                 .code(HttpStatus.OK.value())
                 .timestamp(LocalDateTime.now())
                 .message("Success")
                 .build();
         return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }


    @PostMapping("/getBasicShipmentInfo")
    public ResponseEntity<DataResponse> getBasicShipmentInfo(@RequestBody UserAddress addressTo) throws Exception {
        BasicShippingOrderInfo basicShippingOrderInfo = userAddressService.getBasicShipmentInfo(addressTo);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .data(basicShippingOrderInfo)
                .timestamp(LocalDateTime.now())
                .message("Success")
                .build();

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

}