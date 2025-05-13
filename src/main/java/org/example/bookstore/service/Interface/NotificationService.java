package org.example.bookstore.service.Interface;

import org.example.bookstore.enums.NotificationScope;
import org.example.bookstore.model.Notifications;
import org.example.bookstore.model.User;
import org.example.bookstore.payload.NotificationsDTO;
import org.example.bookstore.payload.response.OffsetResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface NotificationService {


     Notifications save(Notifications notification);
     void saveAll(List<Notifications> notifications);
     void sendNotification(Notifications notification);
     void sendAllNotis(List<Notifications> notiList);
     Notifications getNotificationById(UUID id);
     List<Notifications> getList(User receiver,
                                 NotificationScope scope,
                                 int offset, int limit);
     void markAsRead(UUID notiId);
     void markAllAsRead();
     int countUnread();
     OffsetResponse<NotificationsDTO> getList(NotificationScope scope,
                                              int offset, int limit);


}
