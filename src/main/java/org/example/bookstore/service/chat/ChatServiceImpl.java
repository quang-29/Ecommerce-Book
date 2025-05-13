package org.example.bookstore.service.chat;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ChatServiceImpl implements IChatService {

    private final String BASE_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro:generateContent";

    @Value("${chat.api.key}")
    private String apiKey;

    @Override
    public String getGeminiResponse(String prompt) {
        try {
            String endpoint = BASE_URL + "?key=" + apiKey;

            String requestJson = """
            {
              "contents": [
                {
                  "parts": [
                    {
                      "text": "%s"
                    }
                  ]
                }
              ]
            }
            """.formatted(prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // In response ra để kiểm tra
//            System.out.println("Gemini response: " + response.body());

            JSONObject obj = new JSONObject(response.body());
            if (obj.has("error")) {
                return "Lỗi từ Gemini API: " + obj.getJSONObject("error").getString("message");
            }

            return obj.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "Xin lỗi, có lỗi xảy ra khi gọi Gemini.";
        }
    }
}


