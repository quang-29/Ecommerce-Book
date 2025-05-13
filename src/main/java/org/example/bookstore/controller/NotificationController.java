package org.example.bookstore.controller;


import lombok.RequiredArgsConstructor;
import org.example.bookstore.enums.NotificationScope;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.service.Interface.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

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

    @PostMapping("/mark_as_read")
    public ResponseEntity<?> markAsRead(@RequestParam(required = false) UUID notiId){
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
