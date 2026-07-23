package org.example.bookstore.mapper;

import org.example.bookstore.model.Review;
import org.example.bookstore.payload.ReviewDTO;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReviewMapper(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getId());
        dto.setContent(review.getContent());
        dto.setRatePoint(review.getRatePoint());
        dto.setCreatedAt(review.getCreatedAt());

        if (review.getBookId() != null) {
            bookRepository.findById(review.getBookId()).ifPresent(book -> {
                dto.setBookId(book.getId());
                dto.setTitle(book.getTitle());
                dto.setImagePath(book.getImagePath());
            });
        }

        if (review.getUserId() != null) {
            userRepository.findById(review.getUserId()).ifPresent(user -> {
                dto.setUsername(user.getUsername());
                dto.setAvatarUrl(user.getAvatarUrl());
            });
        }

        return dto;
    }

}
