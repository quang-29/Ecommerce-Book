package org.example.bookstore.service;

import org.example.bookstore.model.ShopAddress;
import org.example.bookstore.model.UserAddress;
import org.example.bookstore.model.address.District;
import org.example.bookstore.model.address.Province;
import org.example.bookstore.model.address.Ward;
import org.example.bookstore.model.shipment.BasicShippingOrderInfo;
import org.example.bookstore.model.shipment.ShipmentInfo;
import org.example.bookstore.payload.request.UserAddressRequest;
import org.example.bookstore.repository.DistrictRepository;
import org.example.bookstore.repository.ProvinceRepository;
import org.example.bookstore.repository.UserAddressRepository;
import org.example.bookstore.repository.WardRepository;
import org.example.bookstore.service.shipment.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressService {


    private final UserAddressRepository userAddressRepository;
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;
    private final WardRepository wardRepository;
    private final GHNService ghnService;

    public UserAddressService(UserAddressRepository userAddressRepository, DistrictRepository districtRepository, ProvinceRepository provinceRepository, WardRepository wardRepository, GHNService ghnService) {
        this.userAddressRepository = userAddressRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.wardRepository = wardRepository;
        this.ghnService = ghnService;
    }


    public List<UserAddress> getAddressListByUser(String username) {
        return userAddressRepository.findByUsername(username) ;
    }

    public void save(UserAddressRequest userAddressRequest) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUsername(userAddressRequest.getUsername());

        if(userAddressRequest.isPrimary()){
            List<UserAddress> userAddressList = userAddressRepository.findAllByUsername(userAddressRequest.getUsername());
            if(userAddressList.size() > 0){
                userAddressList.stream().forEach(userAddress1 -> {
                    userAddress1.setPrimary(false);
                });
            }
        }
        userAddress.setPrimary(userAddressRequest.isPrimary());
        userAddress.setDetail(userAddressRequest.getDetail());
        userAddress.setPhoneNumber(String.valueOf(userAddressRequest.getPhoneNumber()));
        userAddress.setReceiverName(userAddressRequest.getReceiverName());

        int provinceId = ghnService.getProvinceId(userAddressRequest.getProvince());
        Province province = new Province();
        province.setId(provinceId);
        province.setName(userAddressRequest.getProvince());
        provinceRepository.save(province);

        int districtId = ghnService.getDistrictId(provinceId, userAddressRequest.getDistrict());
        District district = new District();
        district.setId(districtId);
        district.setName(userAddressRequest.getDistrict());
        district.setProvinceId(provinceId);
        districtRepository.save(district);

        String wardId = ghnService.getWardCode(districtId, userAddressRequest.getWard());
        Ward ward = new Ward();
        ward.setId(wardId);
        ward.setName(userAddressRequest.getWard());
        ward.setDistrictId(districtId);
        wardRepository.save(ward);

        userAddress.setProvince(province);
        userAddress.setDistrict(district);
        userAddress.setWard(ward);
        userAddressRepository.save(userAddress);
    }

    public List<Province> getProvinceList() {
        return provinceRepository.findAll();
    }

    public Province findProvinceByName(String name) {
        return provinceRepository.findByName(name);
    }
    public List<District> findDistrictByName(String name) {
        return districtRepository.findByName(name);
    }


    public List<Ward> findWardByName(String name) {
        return wardRepository.findByName(name);
    }

    public List<District> findDistrictListByProvinceId(int provinceId) {
        return districtRepository.findAllByProvinceId(provinceId);
    }

    public List<Ward> findWardListByDistrictId(int districtId) {
        return wardRepository.findAllByDistrictId(districtId);
    }

    public BasicShippingOrderInfo getBasicShipmentInfo(UserAddress addressTo) throws Exception {
        ShopAddress addressFrom = new ShopAddress();
        int weight = 4000;
        ShipmentInfo shipmentInfo = new ShipmentInfo();
        shipmentInfo.setWeight(weight);
        shipmentInfo.setFrom(addressFrom);
        shipmentInfo.setTo(addressTo);
        return ghnService.calculateShipmentFee(shipmentInfo);
    }
}
