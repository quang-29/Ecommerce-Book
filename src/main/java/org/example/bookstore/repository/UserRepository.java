package org.example.bookstore.repository;

import jakarta.validation.constraints.Email;
import org.example.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findUserByUsername(String username);

    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(String email);

    User getUserByEmail(@Email String email);

    @Query(value = "select count(id) as totalUser from user;", nativeQuery = true)
    int countUser();
}
