package org.example.bookstore.controller;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.model.UserAddress;
import org.example.bookstore.model.shipment.BasicShippingOrderInfo;
import org.example.bookstore.payload.request.UserAddressRequest;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.service.UserAddressService;
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

    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<ServerResponseDto> getUsername(@PathVariable String username) {

        List<UserAddress> userAddressList = userAddressService.getAddressListByUser(username);
        return ResponseEntity.ok(ServerResponseDto.success(userAddressList));
    }

    @PostMapping("/addUserAddress")
    public ResponseEntity<ServerResponseDto> addUserAddress(@RequestBody UserAddressRequest userAddressRequest) {
         userAddressService.save(userAddressRequest);
         return ResponseEntity.ok(ServerResponseDto.SUCCESS);
    }


    @PostMapping("/getBasicShipmentInfo")
    public ResponseEntity<ServerResponseDto> getBasicShipmentInfo(@RequestBody UserAddress addressTo) throws Exception {
        BasicShippingOrderInfo basicShippingOrderInfo = userAddressService.getBasicShipmentInfo(addressTo);
        return ResponseEntity.ok(ServerResponseDto.success(basicShippingOrderInfo));
    }

}