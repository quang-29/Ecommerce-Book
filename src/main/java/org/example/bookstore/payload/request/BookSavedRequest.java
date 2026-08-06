package org.example.bookstore.payload.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookSavedRequest {
    private String id;
    private String title;
    private String description;
    private Integer volumeNumber;
    private int page;
    private int reprint;
    private long price;
    private Integer discountPercent;
    private MultipartFile avatarUrl;
    private List<MultipartFile> images;
    private Long stock;
    private String publisher;
    private String isbn;
    private String language;
    private String imagePath;
    private String category;
    private String author;
    private LocalDate publishedDate;
}
