package org.example.bookstore.repository;

import org.example.bookstore.model.address.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    List<Ward> findByName(String name);

    List<Ward> findAllByDistrictId(int districtId);
}
