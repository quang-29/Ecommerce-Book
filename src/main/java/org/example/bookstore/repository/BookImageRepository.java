package org.example.bookstore.repository;

import org.example.bookstore.model.BookImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookImageRepository extends JpaRepository<BookImageEntity, Long> {

    List<BookImageEntity> findByBookIdOrderBySortOrderAsc(Long bookId);

}
