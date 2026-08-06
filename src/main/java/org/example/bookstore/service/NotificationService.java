package org.example.bookstore.service;

import jakarta.transaction.Transactional;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.enums.NotificationScope;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.Notifications;
import org.example.bookstore.model.UserEntity;
import org.example.bookstore.payload.NotificationsDTO;
import org.example.bookstore.payload.UserShortenDTO;
import org.example.bookstore.payload.response.OffsetResponse;
import org.example.bookstore.repository.NotificationRepository;
import org.example.bookstore.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final WebPushNotificationService webPushNotificationService;
    private final ModelMapper modelMapper;

    public NotificationService(NotificationRepository notificationRepository,
                               WebPushNotificationService webPushNotificationService,
                               UserRepository userRepository, ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.webPushNotificationService = webPushNotificationService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Notifications save(Notifications notification) {
        return notificationRepository.save(notification);
    }

    public void saveAll(List<Notifications> notifications) {
        notificationRepository.saveAll(notifications);
    }

    public void sendNotification(Notifications notification) {
        Notifications savedNotification = notificationRepository.save(notification);
        webPushNotificationService.send(savedNotification.getReceiverId(), toDTO(savedNotification));
    }

    public void sendAllNotis(List<Notifications> notiList) {
        Iterable<Notifications> savedNotifications = notificationRepository.saveAll(notiList);
        for (Notifications noti : savedNotifications) {
            webPushNotificationService.send(noti.getReceiverId(), toDTO(noti));
        }
    }

    public String getVapidPublicKey() {
        return webPushNotificationService.getPublicKey();
    }

    public Notifications getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.NOTIFICATION_NOT_FOUND));
    }

    public List<Notifications> getList(Long receiverId, NotificationScope scope, int offset, int limit) {
        return notificationRepository.findLastByReceiverIdAndScopeOrderByCreatedAtDesc(
                        receiverId,
                        scope,
                        offset > 0 ? ScrollPosition.offset(offset - 1) : ScrollPosition.offset(),
                        Limit.of(limit))
                .stream().toList();
    }

    @Transactional
    public void markAsRead(Long notiId) {
        Notifications notification = notificationRepository.findById(notiId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.NOTIFICATION_NOT_FOUND));

        UserEntity user = getCurrentUser();
        if (!notification.getReceiverId().equals(user.getId())) {
            throw new ResourceNotFoundException(MessageException.UNAUTHORIZED);
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead() {
        UserEntity user = getCurrentUser();
        List<Notifications> notifications = notificationRepository.findAllByIsReadAndReceiverId(false, user.getId());
        for (Notifications notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    public int countUnread() {
        UserEntity user = getCurrentUser();
        return notificationRepository.countByReceiverIdAndIsRead(user.getId(), false);
    }

    public OffsetResponse<NotificationsDTO> getList(NotificationScope scope, int offset, int limit) {
        UserEntity receiver = getCurrentUser();
        List<Notifications> notiList = getList(receiver.getId(), scope, offset, limit);
        List<NotificationsDTO> dtoList = toDTOList(notiList);
        return new OffsetResponse<>(dtoList, offset + notiList.size());
    }

    private UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));
    }

    private NotificationsDTO toDTO(Notifications notification) {
        NotificationsDTO dto = modelMapper.map(notification, NotificationsDTO.class);
        userRepository.findById(notification.getReceiverId()).ifPresent(receiver -> dto.setReceiver(toShortenDTO(receiver)));
        return dto;
    }

    private List<NotificationsDTO> toDTOList(List<Notifications> notifications) {
        List<Long> receiverIds = notifications.stream().map(Notifications::getReceiverId).distinct().toList();
        Map<Long, UserShortenDTO> receiversById = userRepository.findAllById(receiverIds).stream()
                .collect(java.util.stream.Collectors.toMap(UserEntity::getId, this::toShortenDTO));
        return notifications.stream().map(notification -> {
            NotificationsDTO dto = modelMapper.map(notification, NotificationsDTO.class);
            dto.setReceiver(receiversById.get(notification.getReceiverId()));
            return dto;
        }).toList();
    }

    private UserShortenDTO toShortenDTO(UserEntity user) {
        UserShortenDTO dto = new UserShortenDTO();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
