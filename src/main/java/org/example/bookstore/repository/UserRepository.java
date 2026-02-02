package org.example.bookstore.repository;

import jakarta.validation.constraints.Email;
import org.example.bookstore.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<UserEntity> findUserByUsername(String username);

    @Query("select u from UserEntity u where u.email = :email")
    Optional<UserEntity> findByEmail(String email);

    UserEntity getUserByEmail(@Email String email);

    @Query(value = "select count(id) as totalUser from user;", nativeQuery = true)
    int countUser();
}
