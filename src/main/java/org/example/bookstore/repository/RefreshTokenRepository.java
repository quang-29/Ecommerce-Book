package org.example.bookstore.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.bookstore.model.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Modifying
    @Query("UPDATE RefreshTokenEntity rt SET rt.revoked = true WHERE rt.token =: token AND rt.isDeleted = false ")
    void revokeToken(@Param("token") String token);
}
