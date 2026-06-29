package org.example.bookstore.repository;

import org.example.bookstore.model.BookEntity;
import org.example.bookstore.model.Review;
import org.example.bookstore.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByBookEntityAndUser(BookEntity bookEntity, UserEntity user);


    @Query("select r from Review r where r.user.id = ?1")
    List<Review> findAllReviewsByUserId(Long userId);
}
