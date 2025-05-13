package org.example.bookstore.repository;

import org.example.bookstore.controller.PaymentController;
import org.example.bookstore.enums.PaymentStatus;
import org.example.bookstore.enums.PaymentType;
import org.example.bookstore.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {


    List<Payment> findAllByStatusAndType(PaymentStatus status, PaymentType type);

    @Query(value = "select sum(amount) as total\n" +
            "from payment;",nativeQuery = true)
    long calculateRevenue();
}
