package org.example.bookstore.repository;

import org.example.bookstore.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findAllOrderByUserId(UUID userId, Pageable pageable);

    @Query(value = "select count(id) as totalOrder from orders;",nativeQuery = true)
    int countOrder();

}
