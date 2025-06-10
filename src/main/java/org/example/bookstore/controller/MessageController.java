package org.example.bookstore.controller;

import org.example.bookstore.model.chat.Message;
import org.example.bookstore.repository.MessageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // get All Messages by roomId
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

}
