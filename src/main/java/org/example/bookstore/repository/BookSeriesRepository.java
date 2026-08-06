package org.example.bookstore.repository;

import org.example.bookstore.model.BookSeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookSeriesRepository extends JpaRepository<BookSeriesEntity, Long> {
    Optional<BookSeriesEntity> findByName(String name);

    boolean existsByName(String name);
}
