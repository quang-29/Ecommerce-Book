package org.example.bookstore.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.User;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private UUID reviewId;
    private String content;
    private int ratePoint;
    private LocalDate createdAt;
    private String title;
    private UUID bookId;
    private String imagePath;
    private String username;
    private String avatarUrl;
}
