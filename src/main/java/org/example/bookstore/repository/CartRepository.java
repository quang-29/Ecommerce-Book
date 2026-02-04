package org.example.bookstore.repository;

import org.example.bookstore.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    @Query("select c from CartEntity c where c.user.id = ?1")
    CartEntity getCartByUserId(String userId);
}
