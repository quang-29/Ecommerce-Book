package org.example.bookstore.service.Interface;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.UUID;

public interface PaymentService {
    String getPaymentUrl(UUID orderId, HttpServletRequest request);
    boolean checkPayment(String gateway, Map<String, String> params);
}
