package org.example.bookstore.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.enums.PaymentGateway;
import org.example.bookstore.enums.PaymentStatus;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.OrderEntity;
import org.example.bookstore.model.payment.Payment;
import org.example.bookstore.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class PaymentService {


    private final OrderService orderService;
    private final VNPayService vnPayService;
    private final PaymentRepository paymentRepository;

    public PaymentService(OrderService orderService, VNPayService vnPayService, PaymentRepository paymentRepository) {
        this.orderService = orderService;
        this.vnPayService = vnPayService;
        this.paymentRepository = paymentRepository;
    }


    public String getPaymentUrl(Long orderId, HttpServletRequest request) {
        OrderEntity orderEntity = orderService.getOrderById(orderId);
        if(orderEntity == null)
            throw new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND);
        return vnPayService.createPaymentUrl(orderEntity, request);
    }

    public boolean checkPayment(String gateway, Map<String, String> params) {
        PaymentGateway paymentGateway = PaymentGateway.fromString(gateway);
        if (paymentGateway == PaymentGateway.VNPAY) {
            String txnRef = params.get("vnp_TxnRef");
            String orderIdStr = txnRef.split("-", 2)[0];
            log.info("Check id order txn ref: {}", orderIdStr);
            OrderEntity orderEntity = orderService.getOrderById(Long.valueOf(orderIdStr));
            if (orderEntity == null)
                throw new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND);
            boolean ok = vnPayService.checkPayment(orderEntity, params);
            if(ok){
                Payment payment = paymentRepository.findById(orderEntity.getPaymentId().intValue())
                        .orElseThrow(() -> new ResourceNotFoundException(MessageException.PAYMENT_NOT_FOUND));
                payment.setStatus(PaymentStatus.COMPLETED);
                orderService.savePayment(payment);
            }
            else return false;
        }
        return true;
    }
}
