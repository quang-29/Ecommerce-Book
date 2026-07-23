package org.example.bookstore.service;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.ResourceNotFoundException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    public ServerResponseDto addReview(ReviewCreate reviewCreate) {

        BookEntity bookEntity = bookRepository.findById(reviewCreate.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        UserEntity user = userRepository.findById(reviewCreate.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));

        boolean exists = reviewRepository.existsByBookEntityAndUser(bookEntity, user);
        if (exists) {
            throw new ResourceNotFoundException(MessageException.REVIEW_ALREADY_EXISTS);
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

        return ServerResponseDto.success(reviewDTO);
    }

    @Transactional
    public ServerResponseDto updateReview(ReviewUpdate reviewUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Review review = reviewRepository.findById(reviewUpdate.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.REVIEW_NOT_FOUND));
        if(!review.getUser().getUsername().equals(authentication.getName())) {
            throw new ResourceNotFoundException(MessageException.UNAUTHORIZED_ACTION);
        }

        Long bookId = review.getBookEntity().getId();
        BookEntity bookEntity = bookRepository.findById(bookId)
                        .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        review.setContent(reviewUpdate.getContent());
        review.setRatePoint(reviewUpdate.getRatePoint());
        reviewRepository.save(review);

        List<Review> reviews = bookEntity.getReviews();
        reviews.add(review);
        bookEntity.setReviews(reviews);
        bookEntity.setAverageRating(getAvgRatingProduct(reviews));
        bookRepository.save(bookEntity);

        return ServerResponseDto.success(reviewMapper.mapToDTO(review));
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
    
    public ServerResponseDto deleteReview(Long reviewId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.REVIEW_NOT_FOUND));
        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if(!isAdmin || !review.getUser().getUsername().equals(authentication.getName())) {
            throw new ResourceNotFoundException(MessageException.REVIEW_ERROR_DELETE);
        }
        BookEntity bookEntity = review.getBookEntity();
        List<Review> reviewLists = bookEntity.getReviews();
        reviewLists.remove(review);
        bookEntity.setReviews(reviewLists);
        bookEntity.setAverageRating(getAvgRatingProduct(reviewLists));
        bookRepository.save(bookEntity);
        reviewRepository.delete(review);
        return ServerResponseDto.success("Delete review successfully");
    }

    public ServerResponseDto getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findAllReviewsByUserId(userId);
        List<ReviewDTO> reviewDTOS = reviews.stream().map(review -> {
            ReviewDTO reviewDTO = reviewMapper.mapToDTO(review);
            reviewDTO.setTitle(review.getBookEntity().getTitle());
            reviewDTO.setUsername(review.getUser().getUsername());
            return reviewDTO;
        }).toList();
        return ServerResponseDto.success(reviewDTOS);

    }

    public ServerResponseDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.REVIEW_NOT_FOUND));
        ReviewDTO reviewDTO = reviewMapper.mapToDTO(review);
        reviewDTO.setTitle(review.getBookEntity().getTitle());
        reviewDTO.setUsername(review.getUser().getUsername());
        return ServerResponseDto.success(reviewDTO);
    }

    public ServerResponseDto getReviewsByBookId(Long bookId) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));

        List<Review> reviews = bookEntity.getReviews();
        List<ReviewDTO> reviewDTOS = reviews.stream().map(review ->{
            ReviewDTO reviewDTO = reviewMapper.mapToDTO(review);
            reviewDTO.setTitle(review.getBookEntity().getTitle());
            reviewDTO.setUsername(review.getUser().getUsername());
            return reviewDTO;
        }).toList();
        return ServerResponseDto.success(reviewDTOS);

    }

    public ServerResponseDto getPageReview(String keywordSearch, int page, int size, String sortField, String sortDirection){
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

        if (sortField == null || sortField.isBlank()) {
            sortField = "createdTime";
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortField));
        return ServerResponseDto.success(reviewRepository.getPageReview(keywordSearch, pageable));

    }
}
