package org.example.bookstore.repository;

import org.example.bookstore.model.StoreVoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreVoucherRepository extends JpaRepository<StoreVoucherEntity, Long> {
    Optional<StoreVoucherEntity> findByStoreIdAndVoucherCodeIgnoreCase(Long storeId, String voucherCode);

    List<StoreVoucherEntity> findByStoreId(Long storeId);
}
