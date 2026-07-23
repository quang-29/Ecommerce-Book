package org.example.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "stock", nullable = false)
    private Long stock = 0L;

    @Column(name = "price")
    private Long price;

    @Column(name = "active", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean active = true;

    public long getBasePrice(BookEntity bookEntity) {
        return price == null ? bookEntity.getPrice() : price;
    }

    public int getDiscountPercent(BookEntity bookEntity) {
        Integer discountPercent = bookEntity.getDiscountPercent();
        return discountPercent == null ? 0 : Math.max(0, Math.min(100, discountPercent));
    }

    public long getEffectivePrice(BookEntity bookEntity) {
        return getBasePrice(bookEntity) * (100 - getDiscountPercent(bookEntity)) / 100;
    }

    public long getDiscountAmount(BookEntity bookEntity) {
        return getBasePrice(bookEntity) - getEffectivePrice(bookEntity);
    }
}
