package org.example.bookstore.controller;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.ReviewDTO;
import org.example.bookstore.payload.request.ReviewCreate;
import org.example.bookstore.payload.request.ReviewUpdate;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.Long;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/review")
public class ReviewController {

    private final ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<ServerResponseDto> addReview(@RequestBody ReviewCreate reviewCreate) {
        return ResponseEntity.ok(reviewService.addReview(reviewCreate));
    }

    @PostMapping("/update")
    public ResponseEntity<ServerResponseDto> updateReview(@RequestBody ReviewUpdate reviewUpdate) {
        return ResponseEntity.ok(reviewService.updateReview(reviewUpdate));
    }

    @DeleteMapping("/delete/{reviewId}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> deleteReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.deleteReview(reviewId));
    }

    @GetMapping("/bookId/{bookId}")
    public ResponseEntity<ServerResponseDto> getReviewByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBookId(bookId));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ServerResponseDto> getReviewById(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewById(reviewId));
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<ServerResponseDto> getReviewsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

}
