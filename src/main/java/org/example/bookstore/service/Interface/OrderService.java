package org.example.bookstore.service.Interface;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bookstore.model.Order;
import org.example.bookstore.model.payment.Payment;
import org.example.bookstore.payload.OrderDTO;
import org.example.bookstore.payload.order.PlaceOrderDTO;
import org.example.bookstore.payload.response.OrderResponse;
import org.example.bookstore.payload.response.PlaceOrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface OrderService {

    PlaceOrderResponse placeOrder(PlaceOrderDTO placeOrderDTO, HttpServletRequest request) throws Exception;

    OrderDTO getOrder(UUID orderId);

    List<OrderDTO> getOrdersByUserId(UUID userId);

    List<OrderDTO> getAllOrders();

    OrderDTO updateOrder(UUID orderId, int orderStatus);

    String cancelOrder(UUID orderId);

    String confirmOrder(UUID orderId);

    Order getOrderById(UUID orderId);

    void savePayment(Payment payment);

}
