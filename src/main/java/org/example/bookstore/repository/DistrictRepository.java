package org.example.bookstore.repository;

import org.example.bookstore.model.address.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findByName(String name);

    List<District> findAllByProvinceId(int provinceId);
}
