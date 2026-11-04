package org.example.bookstore.payload;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private Long reviewId;
    private String content;
    private int ratePoint;
    private LocalDate createdAt;
    private String title;
    private Long bookId;
    private String imagePath;
    private String username;
    private String avatarUrl;
}
