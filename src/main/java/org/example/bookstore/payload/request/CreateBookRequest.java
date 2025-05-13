package org.example.bookstore.payload.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {
    private String title;
    private String description;
    private int page;
    private int reprint;
    private long price;
    private Long stock;
    private Long sold;
    private String publisher;
    private String isbn;
    private String language;
    private String imagePath;
    private String category;
    private String author;
    private String publishedDate;
}
