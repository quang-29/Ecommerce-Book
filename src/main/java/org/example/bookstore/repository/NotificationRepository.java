package org.example.bookstore.repository;

import org.example.bookstore.enums.NotificationScope;
import org.example.bookstore.model.Notifications;
import org.example.bookstore.model.UserEntity;
import org.springframework.data.domain.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends CrudRepository<Notifications, Long> {

    List<Notifications> getNotificationsById(Long id);
    List<Notifications> findAllByIsReadAndReceiver(boolean isRead, UserEntity user);
    Window<Notifications> findLastByReceiverAndScopeOrderByCreatedAtDesc(UserEntity receiver,
                                                                         NotificationScope scope,
                                                                         OffsetScrollPosition offset,
                                                                         Limit limit);
    int countByReceiverAndIsRead(UserEntity receiver, boolean b);
}
