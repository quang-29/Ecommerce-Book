package org.example.bookstore.repository;

import org.example.bookstore.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    List<StoreEntity> findByOwnerUserId(Long ownerUserId);
}
