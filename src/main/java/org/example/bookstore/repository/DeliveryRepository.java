package org.example.bookstore.repository;

import org.example.bookstore.model.DeliveryTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryRepository extends JpaRepository<DeliveryTypeEntity, Integer> {


    @Query("select dt from DeliveryTypeEntity dt where dt.deliveryName = ?1")
    DeliveryTypeEntity findByName(String deliveryMethod);
}
