package org.example.bookstore.mapper;

import org.example.bookstore.model.Review;
import org.example.bookstore.payload.ReviewDTO;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getId());
        dto.setContent(review.getContent());
        dto.setRatePoint(review.getRatePoint());
        dto.setCreatedAt(review.getCreatedAt());

        if (review.getBookEntity() != null) {
            dto.setBookId(review.getBookEntity().getId());
            dto.setTitle(review.getBookEntity().getTitle());
            dto.setImagePath(review.getBookEntity().getImagePath());
        }

        if (review.getUser() != null) {
            dto.setUsername(review.getUser().getUsername());
            dto.setAvatarUrl(review.getUser().getAvatarUrl());
        }

        return dto;
    }

}
