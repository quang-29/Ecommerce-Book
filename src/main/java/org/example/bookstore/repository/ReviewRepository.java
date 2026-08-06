package org.example.bookstore.repository;

import org.example.bookstore.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByBookIdAndUserId(Long bookId, Long userId);

    List<Review> findByBookId(Long bookId);

    @Query("select r from Review r where r.userId = ?1")
    List<Review> findAllReviewsByUserId(Long userId);

    @Query(value = "" +
            "SELECT * " +
            "FROM review " +
            "WHERE content LIKE CONCAT('%', :keywordSearch, '%')",
            countQuery = "SELECT COUNT(*) " +
                         "FROM review " +
                         "WHERE content LIKE CONCAT('%', :keywordSearch, '%')" ,
            nativeQuery = true)
    Page<Review> getPageReview(@Param("keywordSearch") String keywordSearch, Pageable pageable);
}
