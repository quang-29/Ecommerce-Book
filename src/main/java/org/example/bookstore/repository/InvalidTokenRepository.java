package org.example.bookstore.repository;

import org.example.bookstore.model.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvalidTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    boolean existsByToken(String token);

    boolean existsById(Long uuid);
}
