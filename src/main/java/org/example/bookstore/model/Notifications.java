package org.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstore.enums.NotificationScope;

import java.util.Date;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationScope scope;

    private int itemCount;

    private String thumbnailUrl = "https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String redirectUrl;

    @Column(name = "is_read")
    private boolean isRead;


    private Date createdAt;
}
