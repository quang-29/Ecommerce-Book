package org.example.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class BookEntity extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "series_id")
    private Long seriesId;

    @Column(name = "volume_number")
    private Integer volumeNumber;

    @Column(name = "price", nullable = false)
    private long price;

    @Column(name = "discount_percent", nullable = false)
    private Integer discountPercent = 0;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "book_description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "language")
    private String language;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "page")
    private int page;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "reprint")
    private int reprint;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "sold", nullable = false)
    private Long sold;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

}
