package org.example.bookstore.repository;

import org.example.bookstore.model.StoreBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
}
