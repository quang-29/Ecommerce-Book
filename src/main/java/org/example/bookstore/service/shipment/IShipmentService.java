package org.example.bookstore.service.shipment;


import org.example.bookstore.model.shipment.BasicShippingOrderInfo;
import org.example.bookstore.model.shipment.ShipmentInfo;

public interface IShipmentService {

    BasicShippingOrderInfo calculateShipmentFee(ShipmentInfo info) throws Exception;

}
