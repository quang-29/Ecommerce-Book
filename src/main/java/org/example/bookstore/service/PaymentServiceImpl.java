package org.example.bookstore.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.enums.PaymentGateway;
import org.example.bookstore.enums.PaymentStatus;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.Order;
import org.example.bookstore.model.payment.Payment;
import org.example.bookstore.service.Interface.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private  VNPayService vnPayService;

    @Override
    public String getPaymentUrl(UUID orderId, HttpServletRequest request) {
        Order order = orderService.getOrderById(orderId);
        if(order == null)
            throw new RuntimeException("Order not found");
        return vnPayService.createPaymentUrl(order, request);
    }

    @Override
    public boolean checkPayment(String gateway, Map<String, String> params) {
        PaymentGateway paymentGateway = PaymentGateway.fromString(gateway);
        if (paymentGateway == PaymentGateway.VNPAY) {
            String txnRef = params.get("vnp_TxnRef");
            String[] p = txnRef.split("-");
            String uuidStr = String.join("-", Arrays.copyOfRange(p, 0, 5)); // Lấy đúng UUID
            log.info("Check id order txn ref: {}", uuidStr);
            Order order = orderService.getOrderById(UUID.fromString(uuidStr));
            if (order == null)
                throw new AppException(ErrorCode.ORDER_NOT_FOUND);
            boolean ok = vnPayService.checkPayment(order, params);
            if(ok){
                Payment payment = order.getPayment();
                payment.setStatus(PaymentStatus.COMPLETED);
                orderService.savePayment(payment);
            }
            else return false;
        }
        return true;
    }
}
