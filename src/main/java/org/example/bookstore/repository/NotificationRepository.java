package org.example.bookstore.repository;

import org.example.bookstore.enums.NotificationScope;
import org.example.bookstore.model.Notifications;
import org.springframework.data.domain.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends CrudRepository<Notifications, Long> {

    List<Notifications> getNotificationsById(Long id);
    List<Notifications> findAllByIsReadAndReceiverId(boolean isRead, Long receiverId);
    List<Notifications> findAllByReceiverId(Long receiverId);
    Window<Notifications> findLastByReceiverIdAndScopeOrderByCreatedAtDesc(Long receiverId,
                                                                         NotificationScope scope,
                                                                         OffsetScrollPosition offset,
                                                                         Limit limit);
    int countByReceiverIdAndIsRead(Long receiverId, boolean b);
}
