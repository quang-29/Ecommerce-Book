package org.example.bookstore.controller;

import org.example.bookstore.service.chat.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatBotController {

    @Autowired
    private IChatService iChatService;

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        String reply = iChatService.getGeminiResponse(message);
        return ResponseEntity.ok(Map.of("reply", reply));
    }
}
