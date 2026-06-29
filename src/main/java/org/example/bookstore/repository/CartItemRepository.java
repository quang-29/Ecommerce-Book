package org.example.bookstore.repository;

import org.example.bookstore.model.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.Long;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    @Query("SELECT ci FROM CartItemEntity ci WHERE ci.cartEntity.id = ?1 AND ci.bookEntity.id = ?2")
    CartItemEntity findCartItemByCartIdAndBookId(Long cartId, Long bookId);

    CartItemEntity findByCartEntityIdAndStoreBookEntityId(Long cartId, Long storeBookId);

    List<CartItemEntity> findByCartEntityId(Long cartId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItemEntity ci WHERE ci.cartEntity.id = ?1 AND ci.bookEntity.id = ?2")
    void deleteCartItemByCartIdAndBookId(Long cartId, Long bookId);

    @Transactional
    @Modifying
    void deleteByCartEntityIdAndStoreBookEntityId(Long cartId, Long storeBookId);

    @Transactional
    @Modifying
    void deleteByCartEntityId(Long cartId);

}
