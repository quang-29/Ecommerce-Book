package org.example.bookstore.controller;

import org.example.bookstore.model.chat.ChatMessage;
import org.example.bookstore.model.chat.Message;
import org.example.bookstore.model.chat.Room;
import org.example.bookstore.repository.MessageRepository;
import org.example.bookstore.repository.RoomRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;

    public ChatController(SimpMessagingTemplate messagingTemplate, RoomRepository roomRepository, MessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/sendMessage/{roomId}")
    public void sendMessage(@Payload ChatMessage chatMessage,
                            @DestinationVariable String roomId) {

        Message message = new Message();
        message.setRoomId(roomId);
        message.setSender(chatMessage.getSender());
        message.setContent(chatMessage.getContent());
        message.setSenderUrl(chatMessage.getSenderUrl());
        message.setSentAt(new Date());
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
        messagingTemplate.convertAndSend("/topic/public", message);
    }

}


