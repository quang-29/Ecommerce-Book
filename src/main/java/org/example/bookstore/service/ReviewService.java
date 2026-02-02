package org.example.bookstore.service;

import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.mapper.ReviewMapper;
import org.example.bookstore.model.BookEntity;
import org.example.bookstore.model.Review;
import org.example.bookstore.model.UserEntity;
import org.example.bookstore.payload.ReviewDTO;
import org.example.bookstore.payload.request.ReviewCreate;
import org.example.bookstore.payload.request.ReviewUpdate;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.ReviewRepository;
import org.example.bookstore.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(BookRepository bookRepository, UserRepository userRepository, ReviewRepository reviewRepository, ModelMapper modelMapper, ReviewMapper reviewMapper) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    public ReviewDTO addReview(ReviewCreate reviewCreate) {

        BookEntity bookEntity = bookRepository.findById(reviewCreate.getBookId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        UserEntity user = userRepository.findById(reviewCreate.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean exists = reviewRepository.existsByBookAndUser(bookEntity, user);
        if (exists) {
            throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }


        Review review = new Review();
        review.setBookEntity(bookEntity);
        review.setCreatedAt(LocalDate.from(LocalDateTime.now()));
        review.setUser(user);
        review.setContent(reviewCreate.getContent());
        review.setRatePoint(reviewCreate.getRating());
        reviewRepository.save(review);

        List<Review> reviews = bookEntity.getReviews();
        reviews.add(review);
        bookEntity.setReviews(reviews);
        bookEntity.setAverageRating(getAvgRatingProduct(reviews));
        bookRepository.save(bookEntity);

        ReviewDTO reviewDTO = reviewMapper.mapToDTO(review);
        reviewDTO.setTitle(review.getBookEntity().getTitle());
        reviewDTO.setUsername(review.getUser().getUsername());

        return reviewDTO;
    }

    @Transactional
    public ReviewDTO updateReview(ReviewUpdate reviewUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Review review = reviewRepository.findById(reviewUpdate.getId())
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        if(!review.getUser().getUsername().equals(authentication.getName())) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        Long bookId = review.getBookEntity().getId();
        BookEntity bookEntity = bookRepository.findById(bookId)
                        .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        review.setContent(reviewUpdate.getContent());
        review.setRatePoint(reviewUpdate.getRatePoint());
        reviewRepository.save(review);

        List<Review> reviews = bookEntity.getReviews();
        reviews.add(review);
        bookEntity.setReviews(reviews);
        bookEntity.setAverageRating(getAvgRatingProduct(reviews));
        bookRepository.save(bookEntity);

        return reviewMapper.mapToDTO(review);
    }
    private double getAvgRatingProduct(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }
        double totalStars = 0;
        for (Review review : reviews) {
            totalStars += review.getRatePoint();
        }
        return totalStars / reviews.size();
    }
    
    public String deleteReview(Long reviewId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if(!isAdmin || !review.getUser().getUsername().equals(authentication.getName())) {
            throw new AppException(ErrorCode.REVIEW_ERROR_DELETE);
        }
        BookEntity bookEntity = review.getBookEntity();
        List<Review> reviewLists = bookEntity.getReviews();
        reviewLists.remove(review);
        bookEntity.setReviews(reviewLists);
        bookEntity.setAverageRating(getAvgRatingProduct(reviewLists));
        bookRepository.save(bookEntity);
        reviewRepository.delete(review);
        return "Delete review successfully";
    }

    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findAllReviewsByUserId(userId);
        return reviews.stream().map(review -> {
            ReviewDTO reviewDTO = reviewMapper.mapToDTO(review);
            reviewDTO.setTitle(review.getBookEntity().getTitle());
            reviewDTO.setUsername(review.getUser().getUsername());
            return reviewDTO;
        }).toList();

    }

    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        ReviewDTO reviewDTO = reviewMapper.mapToDTO(review);
        reviewDTO.setTitle(review.getBookEntity().getTitle());
        reviewDTO.setUsername(review.getUser().getUsername());
        return reviewDTO;
    }

    public List<ReviewDTO> getReviewsByBookId(Long bookId) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        List<Review> reviews = bookEntity.getReviews();
        return reviews.stream().map(review ->{
            ReviewDTO reviewDTO = reviewMapper.mapToDTO(review);
            reviewDTO.setTitle(review.getBookEntity().getTitle());
            reviewDTO.setUsername(review.getUser().getUsername());
            return reviewDTO;
        }).toList();

    }
}
