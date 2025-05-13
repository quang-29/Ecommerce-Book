package org.example.bookstore.repository;

import org.example.bookstore.model.TokenInvalid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvalidTokenRepository extends JpaRepository<TokenInvalid, UUID> {
    boolean existsByToken(String token);

    boolean existsById(UUID uuid);
}
