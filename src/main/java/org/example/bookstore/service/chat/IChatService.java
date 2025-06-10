package org.example.bookstore.service.chat;

public interface IChatService {
    String getGeminiResponse(String prompt);
    String extractBookTitleFromText(String ocrText);
}
