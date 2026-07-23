package org.example.bookstore.repository;

import org.example.bookstore.model.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    
    @Query(value = "" +
            "SELECT b.* " +
            "FROM book b " +
            "JOIN author a ON a.id = b.author_id " +
            "JOIN category c ON c.id = b.category_id " +
            "WHERE b.title LIKE CONCAT('%', :keywordSearch, '%') " +
            "OR a.author_name LIKE CONCAT('%', :keywordSearch, '%')" +
            "OR c.category_name LIKE CONCAT('%', :keywordSearch, '%')"
            ,
            countQuery = "" +
                    "SELECT COUNT(*) " +
                    "FROM book b " +
                    "JOIN author a ON a.id = b.author_id " +
                    "JOIN category c ON c.id = b.category_id " +
                    "WHERE b.title LIKE CONCAT('%', :keywordSearch, '%') " +
                    "OR a.author_name LIKE CONCAT('%', :keywordSearch, '%')" +
                    "OR c.category_name LIKE CONCAT('%', :keywordSearch, '%')",
            nativeQuery = true)
    Page<BookEntity> getPageBook(@Param("keywordSearch") String keywordSearch, Pageable pageable);

    @Query(value = "SELECT * FROM book WHERE sold > 10", nativeQuery = true)
    Page<BookEntity> getBookUpSale(Pageable pageable);

    @Query(value = "" +
            "SELECT b.* " +
            "FROM book b " +
            "JOIN category c ON b.category_id = c.id " +
            "WHERE c.category_name LIKE CONCAT('%', :category, '%')",
            countQuery = "SELECT COUNT(*) " +
                    "FROM book b " +
                    "JOIN category c ON b.category_id = c.id " +
                    "WHERE c.category_name LIKE CONCAT('%', :category, '%')",
            nativeQuery = true)
    Page<BookEntity> getBookByCategory(@Param("category") String category, Pageable pageable);

    @Query(value = "SELECT * FROM book WHERE average_rating > 4.5 AND title LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    Page<BookEntity> getNewReleasedBooks(Pageable pageable, @Param("keyword") String keyword);

    @Query(value = "SELECT * FROM book WHERE title LIKE CONCAT('%', :title, '%')", nativeQuery = true)
    List<BookEntity> getBookByTitle(@Param("title") String title);

    @Query(value = "SELECT COUNT(id) AS totalBook FROM book", nativeQuery = true)
    int countBook();

    @Query(value = "SELECT * FROM book WHERE isbn = :isbn", nativeQuery = true)
    BookEntity findBookByIsbn(@Param("isbn") String isbn);

    BookEntity findAllByTitle(String title);
}