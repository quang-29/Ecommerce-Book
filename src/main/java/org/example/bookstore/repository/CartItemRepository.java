package org.example.bookstore.repository;

import org.example.bookstore.model.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.Long;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    @Query("SELECT ci FROM CartItemEntity ci WHERE ci.cart.id = ?1 AND ci.book.id = ?2")
    CartItemEntity findCartItemByCartIdAndBookId(Long cartId, Long bookId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItemEntity ci WHERE ci.cart.id = ?1 AND ci.book.id = ?2")
    void deleteCartItemByCartIdAndBookId(Long cartId, Long bookId);

}
