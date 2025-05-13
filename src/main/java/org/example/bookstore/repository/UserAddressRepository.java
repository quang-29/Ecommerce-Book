package org.example.bookstore.repository;

import org.example.bookstore.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
    List<UserAddress> findByUsername(String username);


    @Query(value = "SELECT * FROM user_address WHERE username = ?1", nativeQuery = true)
    List<UserAddress> findAllByUsername(String username);



}
