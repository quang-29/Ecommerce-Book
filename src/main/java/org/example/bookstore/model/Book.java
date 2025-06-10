package org.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "price", nullable = false)
    private long price;


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

    @Column(name = "publishedDate", nullable = false)
    private LocalDate publishedDate;


    @OneToMany(mappedBy = "book")
    private List<Review> reviews;

    @OneToMany(mappedBy = "book")
    private List<OrderItem> orderDetails;

    @ManyToMany(mappedBy = "likedBooks")
    private Set<User> likedByUsers = new HashSet<>();


}