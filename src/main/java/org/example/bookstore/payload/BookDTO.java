package org.example.bookstore.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDTO {
    private Long id;
    private String title;
    private String description;
    private Long price;
    private Integer discountPercent;
    private Long discountPrice;
    private String publisher;
    private String isbn;
    private String language;
    private String imagePath;
    private Long stock;
    private Long sold;
    private int page;
    private int reprint;
    private String publishedDate;
    private Double averageRating;
    @JsonProperty("category")
    private String categoryName;
    @JsonProperty("author")
    private String authorName;
    private List<BookImageDTO> images;

}
