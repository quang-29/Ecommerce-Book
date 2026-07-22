package org.example.bookstore.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.example.bookstore.model.StoreBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreBookRepository extends JpaRepository<StoreBookEntity, Long> {
    Optional<StoreBookEntity> findByStoreEntityIdAndBookEntityId(Long storeId, Long bookId);

    Optional<StoreBookEntity> findFirstByBookEntityIdAndStockGreaterThanAndActiveTrueOrderByIdAsc(Long bookId, Long stock);

    @Modifying
    @Query("UPDATE StoreBookEntity sb SET sb.stock = sb.stock - :quantity WHERE sb.id = :storeBookId AND sb.stock >= :quantity AND sb.active = true")
    int decreaseStockIfAvailable(@Param("storeBookId") Long storeBookId, @Param("quantity") Long quantity);

    @Modifying
    @Query("UPDATE StoreBookEntity sb SET sb.stock = sb.stock + :quantity WHERE sb.id = :storeBookId")
    int increaseStock(@Param("storeBookId") Long storeBookId, @Param("quantity") Long quantity);

    // Used by CartService when it needs a consistent read of `stock` while another
    // request may be checking/modifying the same store_book row concurrently.
    // Lock-ordering convention (see CartService): CartEntity is always locked first,
    // this StoreBookEntity lock is acquired second — never the other way round,
    // to avoid a deadlock between two transactions that lock the same two rows
    // in opposite order.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    @Query("select sb from StoreBookEntity sb where sb.id = :storeBookId")
    Optional<StoreBookEntity> findByIdForUpdate(@Param("storeBookId") Long storeBookId);
}
