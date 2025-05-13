package org.example.bookstore.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.bookstore.model.UserAddress;
import org.example.bookstore.model.payment.Payment;
import org.example.bookstore.payload.payment.PaymentDTO;
import org.example.bookstore.payload.userAddress.UserAddressDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {

    private UUID orderId;
    private List<OrderItemDTO> orderItem = new ArrayList<>();
    private Date createAt;
    private Date estimatedDeliveryDate;
    private UserAddressDTO userAddress;
    private PaymentDTO payment;
}
