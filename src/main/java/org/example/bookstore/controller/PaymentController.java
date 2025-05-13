package org.example.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.repository.PaymentRepository;
import org.example.bookstore.service.Interface.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;


    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/payment_url")
    public ResponseEntity<?> getPaymentUrl(@RequestParam UUID orderId,
                                           HttpServletRequest request) {
        return ResponseEntity.ok().body(Map.of(
                "url", paymentService.getPaymentUrl(orderId, request)
        ));
    }

//    @GetMapping("/check")
//    public void checkPayment(@RequestParam String gateway,
//                                          @RequestParam Map<String, String> params,
//                                          HttpServletResponse response) throws IOException {
//        boolean ok = paymentService.checkPayment(gateway, params);
//        String txnRef = params.get("vnp_TxnRef");
//        if(ok){
//            response.sendRedirect("bookstore://payment/success?orderId=" + txnRef);
//        } else {
//            response.sendRedirect("bookstore://payment/failed?orderId=" + txnRef);
//        }
//        return ResponseEntity.ok().body(Map.of(
//                "status", ok ? "success" : "failed"
//        ));
//    }


    @GetMapping("/check")
    public void checkPayment(@RequestParam String gateway,
                             @RequestParam Map<String, String> params,
                             HttpServletResponse response) throws IOException {
        try {
            boolean ok = paymentService.checkPayment(gateway, params);
            String txnRef = params.get("vnp_TxnRef");

            log.info("Payment callback - Gateway: {}, TxnRef: {}, Status: {}",
                    gateway, txnRef, ok ? "SUCCESS" : "FAILED");

            if (txnRef == null) {
                log.error("Transaction reference is null");
                txnRef = "UNKNOWN";
            }

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            // Deeplink URL cho ứng dụng đã cài đặt
            String deeplinkUrl = ok ?
                    "bookstore://payment/success?orderId=" + txnRef :
                    "bookstore://payment/failed?orderId=" + txnRef;

            // Fallback URL cho trường hợp không mở được app
            String fallbackUrl = "https://your-website.com/order/" + txnRef;

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("<title>Chuyển hướng về ứng dụng</title>");
            out.println("<script type=\"text/javascript\">");
            out.println("function openApp() {");
            out.println("    // Thử mở ứng dụng bằng deeplink");
            out.println("    window.location.href = \"" + deeplinkUrl + "\";");
            out.println("    // Fallback sau 3 giây");
            out.println("    setTimeout(function() {");
            out.println("        document.getElementById('message').style.display = 'block';");
            out.println("    }, 3000);");
            out.println("}");
            out.println("window.onload = openApp;");
            out.println("</script>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; text-align: center; padding: 50px 20px; }");
            out.println("#message { display: none; margin-top: 20px; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>" + (ok ? "Thanh toán thành công!" : "Thanh toán thất bại!") + "</h2>");
            out.println("<p>Đang chuyển về ứng dụng...</p>");
            out.println("<div id=\"message\">");
            out.println("<p>Nếu ứng dụng không tự động mở, vui lòng thử một trong các cách sau:</p>");
            out.println("<div style=\"margin-top: 20px;\">");
            out.println("<a href=\"javascript:openApp()\" style=\"display:inline-block; background-color:#4a90e2; color:white; padding:12px 30px; text-decoration:none; border-radius:5px; font-weight:bold; margin-right: 10px;\">Mở ứng dụng</a>");
            out.println("<a href=\"" + fallbackUrl + "\" style=\"display:inline-block; background-color:#2ecc71; color:white; padding:12px 30px; text-decoration:none; border-radius:5px; font-weight:bold;\">Xem đơn hàng</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            log.error("Error processing payment callback", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing payment");
        }
    }

    @GetMapping("/calculateRevenue")
    public ResponseEntity<?> calculateRevenue() {
        long revenue = paymentRepository.calculateRevenue();
        return ResponseEntity.ok(revenue);
    }
}
