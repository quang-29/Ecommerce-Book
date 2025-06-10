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

    private final String BASE_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent";

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
            """.formatted(prompt.replace("\"", "\\\"")); // escape dấu "

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

    @Override
    public String extractBookTitleFromText(String ocrText) {
        try {
            System.out.println("OCR Text: " + ocrText); // để kiểm tra đầu vào

            String prompt = """
                Tôi có một đoạn văn bản được trích xuất từ ảnh bìa sách bằng công nghệ OCR, nhưng có thể bị sai chính tả và lẫn nhiều thông tin không liên quan như tác giả, dịch giả, nhà xuất bản.
            
                Đoạn văn:
                ---
                %s
                ---
            
                Hãy thực hiện các bước sau:
                1.Nếu tiêu đề là Tiếng Việt thì sửa lỗi chính tả tiếng Việt, nếu không phải thì thôi.
                2. Xác định đâu là tên cuốn sách chính có trong đoạn văn.
                3. Chỉ trả về đúng tên sách, không thêm gì khác.
            
                Nếu không xác định được rõ tên sách, hãy trả về: "Không xác định được tiêu đề".
    """.formatted(ocrText);

            String result = getGeminiResponse(prompt);
            if (result == null || result.trim().isEmpty()) {
                return "Không xác định được tiêu đề";
            }

            return result.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi gửi yêu cầu đến Gemini";
        }
    }
}
