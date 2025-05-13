package org.example.bookstore.repository;

import org.example.bookstore.enums.NotificationScope;
import org.example.bookstore.model.Notifications;
import org.example.bookstore.model.User;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends CrudRepository<Notifications, UUID> {

    List<Notifications> getNotificationsById(UUID id);
    List<Notifications> findAllByIsReadAndReceiver(boolean isRead, User user);
    Window<Notifications> findLastByReceiverAndScopeOrderByCreatedAtDesc(User receiver,
                                                                        NotificationScope scope,
                                                                        OffsetScrollPosition offset,
                                                                        Limit limit);
    int countByReceiverAndIsRead(User receiver, boolean b);
}
