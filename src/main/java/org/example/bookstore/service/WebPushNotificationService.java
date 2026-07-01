package org.example.bookstore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.UserEntity;
import org.example.bookstore.model.WebPushSubscription;
import org.example.bookstore.payload.NotificationsDTO;
import org.example.bookstore.payload.request.WebPushSubscriptionRequest;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.repository.WebPushSubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.List;

@Service
public class WebPushNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebPushNotificationService.class);

    private final WebPushSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private PushService pushService;

    @Value("${web-push.vapid.public-key:}")
    private String publicKey;

    @Value("${web-push.vapid.private-key:}")
    private String privateKey;

    @Value("${web-push.vapid.subject:}")
    private String subject;

    @Value("${domain-staff-fe}")
    String domainStaffFE;

    @Value("${api-url}")
    String apiUrl;

    public WebPushNotificationService(WebPushSubscriptionRepository subscriptionRepository,
                                      UserRepository userRepository,
                                      ObjectMapper objectMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void init() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        if (!isBlank(publicKey) && !isBlank(privateKey) && !isBlank(subject)) {
            pushService = new PushService(publicKey, privateKey, subject);
        }
    }

    public String getPublicKey() {
        return publicKey == null ? "" : publicKey;
    }

    @Transactional
    public void subscribe(WebPushSubscriptionRequest request) {
        validateSubscription(request);
        UserEntity user = getCurrentUser();
        WebPushSubscription subscription = subscriptionRepository.findByEndpoint(request.getEndpoint())
                .orElseGet(WebPushSubscription::new);
        subscription.setUser(user);
        subscription.setEndpoint(request.getEndpoint());
        subscription.setP256dh(request.getKeys().getP256dh());
        subscription.setAuth(request.getKeys().getAuth());
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void unsubscribe(WebPushSubscriptionRequest request) {
        if (request != null && request.getEndpoint() != null) {
            subscriptionRepository.deleteByEndpoint(request.getEndpoint());
        }
    }

    public void send(UserEntity user, NotificationsDTO notification) {
        if (pushService == null || user == null) {
            return;
        }
        List<WebPushSubscription> subscriptions = subscriptionRepository.findAllByUser(user);
        if (subscriptions.isEmpty()) {
            return;
        }
        String payload = toPayload(notification);
        for (WebPushSubscription subscription : subscriptions) {
            send(subscription, payload);
        }
    }

    private void send(WebPushSubscription subscription, String payload) {
        try {
            Notification notification = new Notification(
                    subscription.getEndpoint(),
                    subscription.getP256dh(),
                    subscription.getAuth(),
                    payload
            );
            HttpResponse response = pushService.send(notification);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 404 || statusCode == 410) {
                subscriptionRepository.delete(subscription);
            }
        } catch (Exception ex) {
            LOGGER.warn("Cannot send web push notification to endpoint {}", subscription.getEndpoint(), ex);
        }
    }

    private String toPayload(NotificationsDTO notification) {
        try {
            return objectMapper.writeValueAsString(notification);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Cannot serialize notification payload", ex);
        }
    }

    private void validateSubscription(WebPushSubscriptionRequest request) {
        if (request == null
                || isBlank(request.getEndpoint())
                || request.getKeys() == null
                || isBlank(request.getKeys().getP256dh())
                || isBlank(request.getKeys().getAuth())) {
            throw new ResourceNotFoundException(MessageException.INVALID_REQUEST);
        }
    }

    private UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
