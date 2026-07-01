package org.example.bookstore.controller;


import lombok.RequiredArgsConstructor;
import org.example.bookstore.enums.NotificationScope;
import org.example.bookstore.payload.request.WebPushSubscriptionRequest;
import org.example.bookstore.service.NotificationService;
import org.example.bookstore.service.WebPushNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final WebPushNotificationService webPushNotificationService;

    @GetMapping("/get_list")
    public ResponseEntity<?> getNotificationList(@RequestParam String scope,
                                                 @RequestParam int offset,
                                                 @RequestParam int limit) {
        NotificationScope scp;
        try{
            scp = NotificationScope.fromString(scope);
        }
        catch (IllegalArgumentException e){
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok(notificationService.getList(
                scp, offset, limit
        ));
    }

    @GetMapping("/count_unread")
    public ResponseEntity<?> countUnread(){

        return ResponseEntity.ok(notificationService.countUnread());
    }

    @GetMapping("/vapid-public-key")
    public ResponseEntity<?> getVapidPublicKey() {
        return ResponseEntity.ok(Map.of(
                "publicKey", notificationService.getVapidPublicKey()
        ));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody WebPushSubscriptionRequest request) {
        webPushNotificationService.subscribe(request);
        return ResponseEntity.ok(Map.of(
                "status", "success"
        ));
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestBody WebPushSubscriptionRequest request) {
        webPushNotificationService.unsubscribe(request);
        return ResponseEntity.ok(Map.of(
                "status", "success"
        ));
    }

    @PostMapping("/mark_as_read")
    public ResponseEntity<?> markAsRead(@RequestParam(required = false) Long notiId){
        if(notiId == null){
            notificationService.markAllAsRead();
        }
        else{
            notificationService.markAsRead(notiId);
        }
        return ResponseEntity.ok(Map.of(
                "status", "success"
        ));
    }

}
