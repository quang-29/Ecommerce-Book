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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "store_book",
        uniqueConstraints = @UniqueConstraint(name = "uk_store_book_store_book", columnNames = {"store_id", "book_id"})
)
public class StoreBookEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity storeEntity;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity bookEntity;

    @Column(name = "stock", nullable = false)
    private Long stock = 0L;

    @Column(name = "price")
    private Long price;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public long getBasePrice() {
        return price == null ? bookEntity.getPrice() : price;
    }

    public int getDiscountPercent() {
        Integer discountPercent = bookEntity.getDiscountPercent();
        return discountPercent == null ? 0 : Math.max(0, Math.min(100, discountPercent));
    }

    public long getEffectivePrice() {
        return getBasePrice() * (100 - getDiscountPercent()) / 100;
    }

    public long getDiscountAmount() {
        return getBasePrice() - getEffectivePrice();
    }
}
