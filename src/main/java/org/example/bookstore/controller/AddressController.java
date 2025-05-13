package org.example.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.model.address.District;
import org.example.bookstore.model.address.Ward;
import org.example.bookstore.service.Interface.UserAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
public class AddressController {

    private final UserAddressService userAddressService;

    @GetMapping("/provinces")
    public ResponseEntity<?> getProvinceList(){

        return ResponseEntity.ok(userAddressService.getProvinceList());
    }

    @GetMapping("/districts")
    public ResponseEntity<?> getDistrictList(@RequestParam int provinceId){
        List<District> res = userAddressService.findDistrictListByProvinceId(provinceId);
        if(res == null || res.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/wards")
    public ResponseEntity<?> getWardList(@RequestParam int districtId){
        List<Ward> res = userAddressService.findWardListByDistrictId(districtId);
        if(res == null || res.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res);
    }

}
