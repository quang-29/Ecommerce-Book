package org.example.bookstore.repository;

import org.example.bookstore.model.Order;
import org.example.bookstore.payload.order.OrderInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByUserId(UUID userId);

    @Query(value = "select count(id) as totalOrder from orders;",nativeQuery = true)
    int countOrder();



//    List<OrderInfoDTO> getAllOrders();
}
