package org.example.bookstore.repository;

import org.example.bookstore.model.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.token = :token AND rt.revoked = false AND rt.isDeleted = false")
    Optional<RefreshTokenEntity> findActiveToken(@Param("token") String token);

    @Modifying
    @Query("UPDATE RefreshTokenEntity rt SET rt.revoked = true WHERE rt.token = :token AND rt.isDeleted = false")
    void revokeToken(@Param("token") String token);
}
