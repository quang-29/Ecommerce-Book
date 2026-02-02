package org.example.bookstore.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.config.VNPAYConfig;
import org.example.bookstore.enums.PaymentStatus;
import org.example.bookstore.enums.PaymentType;
import org.example.bookstore.model.OrderEntity;
import org.example.bookstore.model.payment.Payment;
import org.example.bookstore.utils.VNPayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VNPayService {

    private final VNPAYConfig vnPayConfig;

    @Value("${payment.vnPay.max_time}")
    private int maxPaymentTime;

    private final String SUCCESS_CODE = "00";

    public String createPaymentUrl(OrderEntity orderEntity,
                                   HttpServletRequest request){
        Payment payment = orderEntity.getPayment();
        if(payment.getType() != PaymentType.BANK_TRANSFER)
            throw new RuntimeException("Payment type is not BANK_TRANSFER");
        if(payment.getStatus() == PaymentStatus.EXPIRED)
            throw new RuntimeException("Payment expired");
        if(payment.getStatus() != PaymentStatus.PENDING)
            throw new RuntimeException("Payment status is not PENDING");
        Date expiredDate = payment.getExpireAt();
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        Date currentTime = new Date();
        if(currentTime.after(expiredDate)){
            payment.setStatus(PaymentStatus.EXPIRED);
            orderEntity.setPayment(payment);
            throw new RuntimeException("Payment expired");
        }
        Map<String, String> params = vnPayConfig.getConfig();
        long diffInMillis = expiredDate.getTime() - currentTime.getTime();
        int remainingSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(diffInMillis);
        int allowedTime = Math.min(remainingSeconds, maxPaymentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(now.getTime());
        params.put("vnp_CreateDate", vnp_CreateDate);
        now.add(Calendar.SECOND, allowedTime);
        String vnp_ExpireDate = formatter.format(now.getTime());
        params.put("vnp_ExpireDate", vnp_ExpireDate);
        params.put("vnp_Amount", String.valueOf(payment.getAmount() * 100L));
        String ref = orderEntity.getId() + "-" + System.currentTimeMillis();
        params.put("vnp_TxnRef", ref);
        params.put("vnp_OrderInfo", "Thanh toan don hang: " + orderEntity.getId());
        String ipAddr = VNPayUtil.getIpAddress(request);
        params.put("vnp_IpAddr", ipAddr);
        String queryString = VNPayUtil.createPaymentUrl(params, true);
        String hashData = VNPayUtil.createPaymentUrl(params, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryString += "&vnp_SecureHash=" + vnpSecureHash;
        return vnPayConfig.getVnp_PayUrl() + "?" + queryString;
    }

    public boolean checkPayment(OrderEntity orderEntity, Map<String, String> params) {
        String code = params.get("vnp_ResponseCode");
        if(code.equals(SUCCESS_CODE)){
            long amount = Long.parseLong(params.get("vnp_Amount")) / 100L;
            Payment payment = orderEntity.getPayment();
            return amount == payment.getAmount();
        }
        else return false;
    }
}
