package org.example.bookstore.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.example.bookstore.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    @Query("select c from CartEntity c where c.userId = ?1")
    CartEntity getCartByUserId(String userId);

    Optional<CartEntity> findByUserId(Long userId);

    // Lock-ordering convention (see CartService): this Cart lock is always acquired
    // BEFORE any StoreBookEntity lock, never after, to avoid a deadlock where two
    // concurrent transactions each hold one row and wait on the other's row.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    @Query("select c from CartEntity c where c.id = :cartId")
    Optional<CartEntity> findByIdForUpdate(Long cartId);
}
