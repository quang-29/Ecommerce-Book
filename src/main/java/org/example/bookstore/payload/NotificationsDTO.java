package org.example.bookstore.payload;

import lombok.Getter;
import lombok.Setter;
import org.example.bookstore.enums.NotificationScope;

import java.util.Date;

@Getter
@Setter
public class NotificationsDTO {

    private String content;
    private NotificationScope scope;
    private int itemCount;
    private String thumbnailUrl = "https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png";
    private UserShortenDTO receiver;
    private String redirectUrl;
    private boolean isRead;
    private Date createdAt;
}
