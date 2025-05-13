package org.example.bookstore.service.Interface;

import org.example.bookstore.model.UserAddress;
import org.example.bookstore.model.address.Address;
import org.example.bookstore.model.address.District;
import org.example.bookstore.model.address.Province;
import org.example.bookstore.model.address.Ward;
import org.example.bookstore.model.shipment.BasicShippingOrderInfo;
import org.example.bookstore.model.shipment.ShipmentInfo;
import org.example.bookstore.payload.request.UserAddressRequest;
import org.example.bookstore.payload.userAddress.UserAddressDTO;

import java.util.List;

public interface UserAddressService {

    List<UserAddress> getAddressListByUser(String username);

    void save(UserAddressRequest userAddressRequest);

    List<Province> getProvinceList();

    Province findProvinceByName(String name);

    List<District> findDistrictByName(String name);

    List<Ward> findWardByName(String name);

    List<District> findDistrictListByProvinceId(int provinceId);

    List<Ward> findWardListByDistrictId(int districtId);

    BasicShippingOrderInfo getBasicShipmentInfo(UserAddress addressTo) throws Exception;
}
