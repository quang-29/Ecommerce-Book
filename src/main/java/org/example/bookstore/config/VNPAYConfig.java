package org.example.bookstore.config;

import lombok.Getter;
import org.example.bookstore.utils.VNPayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


@Configuration
public class VNPAYConfig {
    @Getter
    @Value("${payment.vnPay.url}")
    private String vnp_PayUrl;

    @Getter
    @Value("${payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;

    @Getter
    @Value("${payment.vnPay.tmnCode}")
    private String vnp_TmnCode ;

    @Getter
    @Value("${payment.vnPay.secretKey}")
    private String secretKey;

    @Getter
    @Value("${payment.vnPay.version}")
    private String vnp_Version;


    @Value("${payment.vnPay.command}")
    private String vnp_Command;

    @Value("${payment.vnPay.orderType}")
    private String orderType;

    public Map<String, String> getConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
        return vnpParamsMap;
    }
}
