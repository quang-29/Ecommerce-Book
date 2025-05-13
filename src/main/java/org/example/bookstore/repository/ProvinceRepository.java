package org.example.bookstore.repository;

import org.example.bookstore.model.address.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {

    Province findByName(String name);
}
