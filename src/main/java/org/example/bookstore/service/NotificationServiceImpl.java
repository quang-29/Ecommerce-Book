package org.example.bookstore.service;

import jakarta.transaction.Transactional;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.enums.NotificationScope;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.Notifications;
import org.example.bookstore.model.User;
import org.example.bookstore.payload.NotificationsDTO;
import org.example.bookstore.payload.response.OffsetResponse;
import org.example.bookstore.repository.NotificationRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.Interface.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ModelMapper modelMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   SimpMessagingTemplate simpMessagingTemplate,
                                   UserRepository userRepository, ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Notifications save(Notifications notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void saveAll(List<Notifications> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void sendNotification(Notifications notification) {
        notificationRepository.save(notification);
        simpMessagingTemplate.convertAndSendToUser(
                notification.getReceiver().getUsername(),
                "/notify",
                toDTO(notification)
        );
    }

    @Override
    public void sendAllNotis(List<Notifications> notiList) {
        notificationRepository.saveAll(notiList);
        for (Notifications noti : notiList) {
            simpMessagingTemplate.convertAndSendToUser(
                    noti.getReceiver().getUsername(),
                    "/notify",
                    toDTO(noti)
            );
        }
    }

    @Override
    public Notifications getNotificationById(UUID id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

    @Override
    public List<Notifications> getList(User receiver, NotificationScope scope, int offset, int limit) {
        return notificationRepository.findLastByReceiverAndScopeOrderByCreatedAtDesc(
                        receiver,
                        scope,
                        offset > 0 ? ScrollPosition.offset(offset - 1) : ScrollPosition.offset(),
                        Limit.of(limit))
                .stream().toList();
    }

    @Override
    @Transactional
    public void markAsRead(UUID notiId) {
        Notifications notification = notificationRepository.findById(notiId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        User user = getCurrentUser();
        if (!notification.getReceiver().equals(user)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead() {
        User user = getCurrentUser();
        List<Notifications> notifications = notificationRepository.findAllByIsReadAndReceiver(false, user);
        for (Notifications notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    @Override
    public int countUnread() {
        User user = getCurrentUser();
        return notificationRepository.countByReceiverAndIsRead(user, false);
    }

    @Override
    public OffsetResponse<NotificationsDTO> getList(NotificationScope scope, int offset, int limit) {
        User receiver = getCurrentUser();
        List<Notifications> notiList = getList(receiver, scope, offset, limit);
        List<NotificationsDTO> dtoList = notiList.stream().map(this::toDTO).toList();
        return new OffsetResponse<>(dtoList, offset + notiList.size());
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private NotificationsDTO toDTO(Notifications notification) {

        return modelMapper.map(notification, NotificationsDTO.class);
    }
}
