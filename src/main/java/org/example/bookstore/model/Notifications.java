package org.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstore.enums.NotificationScope;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notifications extends BaseEntity {

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationScope scope;

    private int itemCount;

    private String thumbnailUrl = "https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    private String redirectUrl;

    @Column(name = "is_read")
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean isRead;


    private Date createdAt;
}
