package org.example.bookstore.model;

import jakarta.persistence.*;
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
@Table(name = "review")
public class Review extends BaseEntity {

    @Column(name = "content")
    private String content;

    @Column(name = "rate_point")
    private int ratePoint;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity bookEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}