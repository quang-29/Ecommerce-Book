package org.example.bookstore.controller;

import org.example.bookstore.model.chat.Message;
import org.example.bookstore.repository.MessageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getAllMessages(@PathVariable("roomId") String roomId) {
        List<Message> messages = messageRepository.findAllByRoomId(roomId);
        return new ResponseEntity<>(messages, HttpStatus.OK);

    }

    @GetMapping("/getRecentMessage/{roomId}")
    public ResponseEntity<?> getRecentMessages(@PathVariable("roomId") String roomId) {
        Message message = messageRepository.findRecentMessageByRoomId(roomId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @PutMapping("/updateAvatarUrl")
    public ResponseEntity<?> updateAvatarUrl(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String newAvatarUrl = request.get("newAvatarUrl");

        if (username == null || newAvatarUrl == null) {
            return ResponseEntity.badRequest().body("Thiếu thông tin.");
        }

        int updated = messageRepository.updateUrlSenderBySender(username, newAvatarUrl);
        return ResponseEntity.ok(Map.of("success", true, "updatedCount", updated));
    }

}
