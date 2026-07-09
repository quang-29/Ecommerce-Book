package org.example.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "store_voucher",
        uniqueConstraints = @UniqueConstraint(name = "uk_store_voucher_store_code", columnNames = {"store_id", "voucher_code"})
)
public class StoreVoucherEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity storeEntity;

    @Column(name = "voucher_code", nullable = false)
    private String voucherCode;

    @Column(name = "discount_percent", nullable = false)
    private Integer discountPercent = 0;

    @Column(name = "discount_amount", nullable = false)
    private Long discountAmount = 0L;

    @Column(name = "min_order_amount", nullable = false)
    private Long minOrderAmount = 0L;

    @Column(name = "active", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean active = true;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    public long calculateDiscount(long storeSubtotal) {
        if (storeSubtotal < nullToZero(minOrderAmount)) {
            return 0L;
        }
        int percent = discountPercent == null ? 0 : Math.max(0, Math.min(100, discountPercent));
        long percentDiscount = storeSubtotal * percent / 100;
        long fixedDiscount = nullToZero(discountAmount);
        return Math.min(storeSubtotal, percentDiscount + fixedDiscount);
    }

    public boolean isUsableNow() {
        LocalDateTime now = LocalDateTime.now();
        return active
                && (startAt == null || !startAt.isAfter(now))
                && (endAt == null || !endAt.isBefore(now));
    }

    private long nullToZero(Long value) {
        return value == null ? 0L : value;
    }
}
