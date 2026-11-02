package org.example.bookstore.model;

import jakarta.persistence.Entity;
import lombok.*;
import org.example.bookstore.enums.Amount;
import org.example.bookstore.enums.UserLimit;
import org.example.bookstore.enums.VoucherType;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Voucher extends BaseEntity {

    private String code;
    private VoucherType voucherType;
    private Amount amount;
    private Long maxDiscount;
    private Long minOrderValue;
    private String storeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long usageLimit;
    private Long usedCount;
    private UserLimit perUserLimit;
    private boolean active;

}
