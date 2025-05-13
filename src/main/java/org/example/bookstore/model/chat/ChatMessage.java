package org.example.bookstore.model.chat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class ChatMessage {
    private String roomId;
    private String sender;
    private String content;
    private LocalDate sentAt;
}

