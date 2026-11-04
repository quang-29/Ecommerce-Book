package org.example.bookstore.repository;

import org.example.bookstore.model.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.example.bookstore.enums.PaymentStatus;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Page<OrderEntity> findAllOrderByUserId(Long userId, Pageable pageable);

    java.util.List<OrderEntity> findAllByUserId(Long userId);

    Optional<OrderEntity> findByPaymentId(Long paymentId);

    @Query(value = "select o from OrderEntity o where exists (select 1 from OrderItem oi, StoreBookEntity sb "
            + "where oi.orderId = o.id and oi.storeBookId = sb.id and sb.storeId = :storeId) "
            + "and (:status is null or exists (select 1 from Payment p where p.id = o.paymentId and p.status = :status))",
            countQuery = "select count(o) from OrderEntity o where exists (select 1 from OrderItem oi, StoreBookEntity sb "
                    + "where oi.orderId = o.id and oi.storeBookId = sb.id and sb.storeId = :storeId) "
                    + "and (:status is null or exists (select 1 from Payment p where p.id = o.paymentId and p.status = :status))")
    Page<OrderEntity> findByStore(@Param("storeId") Long storeId, @Param("status") PaymentStatus status, Pageable pageable);

    @Query("select count(o) > 0 from OrderEntity o where o.id = :orderId and exists (select 1 from OrderItem oi, StoreBookEntity sb "
            + "where oi.orderId = o.id and oi.storeBookId = sb.id and sb.storeId = :storeId)")
    boolean existsByIdAndStore(@Param("orderId") Long orderId, @Param("storeId") Long storeId);

    @Query(value = "select count(id) as totalOrder from orders;",nativeQuery = true)
    int countOrder();

}
