package org.example.bookstore.repository;

import org.example.bookstore.model.UserLikedBookEntity;
import org.example.bookstore.model.UserLikedBookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserLikedBookRepository extends JpaRepository<UserLikedBookEntity, UserLikedBookId> {

    List<UserLikedBookEntity> findByUserId(Long userId);

    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    @Transactional
    @Modifying
    void deleteByUserIdAndBookId(Long userId, Long bookId);
}
