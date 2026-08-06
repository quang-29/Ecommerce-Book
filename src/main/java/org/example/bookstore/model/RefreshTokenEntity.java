package org.example.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.checkerframework.checker.signature.qual.BinaryName;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "refresh_token")
public class RefreshTokenEntity extends BaseEntity {

    private String token;
    private Long userId;
    private LocalDateTime expiredDate;
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean revoked;
    private String userAgent;
    private String ipAddress;
}
