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
import org.springframework.data.domain.Page;
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

        boolean exists = reviewRepository.existsByBookIdAndUserId(bookEntity.getId(), user.getId());
        if (exists) {
            throw new ResourceNotFoundException(MessageException.REVIEW_ALREADY_EXISTS);
        }

        Review review = new Review();
        review.setBookId(bookEntity.getId());
        review.setCreatedAt(LocalDate.from(LocalDateTime.now()));
        review.setUserId(user.getId());
        review.setContent(reviewCreate.getContent());
        review.setRatePoint(reviewCreate.getRating());
        reviewRepository.save(review);

        List<Review> reviews = reviewRepository.findByBookId(bookEntity.getId());
        bookEntity.setAverageRating(getAvgRatingProduct(reviews));
        bookRepository.save(bookEntity);

        ReviewDTO reviewDTO = reviewMapper.mapToDTO(review);

        return ServerResponseDto.success(reviewDTO);
    }

    @Transactional
    public ServerResponseDto updateReview(ReviewUpdate reviewUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Review review = reviewRepository.findById(reviewUpdate.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.REVIEW_NOT_FOUND));
        UserEntity reviewOwner = userRepository.findById(review.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));
        if(!reviewOwner.getUsername().equals(authentication.getName())) {
            throw new ResourceNotFoundException(MessageException.UNAUTHORIZED_ACTION);
        }

        Long bookId = review.getBookId();
        BookEntity bookEntity = bookRepository.findById(bookId)
                        .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        review.setContent(reviewUpdate.getContent());
        review.setRatePoint(reviewUpdate.getRatePoint());
        reviewRepository.save(review);

        List<Review> reviews = reviewRepository.findByBookId(bookId);
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
        UserEntity reviewOwner = userRepository.findById(review.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));
        if(!isAdmin || !reviewOwner.getUsername().equals(authentication.getName())) {
            throw new ResourceNotFoundException(MessageException.REVIEW_ERROR_DELETE);
        }
        BookEntity bookEntity = bookRepository.findById(review.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        reviewRepository.delete(review);
        List<Review> reviewLists = reviewRepository.findByBookId(bookEntity.getId());
        bookEntity.setAverageRating(getAvgRatingProduct(reviewLists));
        bookRepository.save(bookEntity);
        return ServerResponseDto.success("Delete review successfully");
    }

    public ServerResponseDto getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findAllReviewsByUserId(userId);
        List<ReviewDTO> reviewDTOS = reviews.stream().map(reviewMapper::mapToDTO).toList();
        return ServerResponseDto.success(reviewDTOS);

    }

    public ServerResponseDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.REVIEW_NOT_FOUND));
        ReviewDTO reviewDTO = reviewMapper.mapToDTO(review);
        return ServerResponseDto.success(reviewDTO);
    }

    public ServerResponseDto getReviewsByBookId(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND);
        }
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        List<ReviewDTO> reviewDTOS = reviews.stream().map(reviewMapper::mapToDTO).toList();
        return ServerResponseDto.success(reviewDTOS);

    }

    public ServerResponseDto getPageReview(String keywordSearch, int page, int size, String sortField, String sortDirection){
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

        if (sortField == null || sortField.isBlank()) {
            sortField = "createdTime";
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortField));
        Page<ReviewDTO> reviewDTOPage = reviewRepository.getPageReview(keywordSearch, pageable)
                .map(reviewMapper::mapToDTO);
        return ServerResponseDto.success(reviewDTOPage);

    }
}
