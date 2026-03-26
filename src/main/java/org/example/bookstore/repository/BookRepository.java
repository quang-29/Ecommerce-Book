package org.example.bookstore.repository;

import org.example.bookstore.model.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.Long;
import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    @Query(value = "SELECT * FROM book WHERE title COLLATE utf8mb4_bin LIKE CONCAT('%', :title, '%')", nativeQuery = true)
    BookEntity findByName(String title);

    Page<BookEntity> findByCategory_Name(String category, Pageable pageable);
    Page<BookEntity> findByAuthor_Name(String authorName, Pageable pageable);

    @Query(value = "SELECT * FROM book  WHERE sold > 10", nativeQuery = true)
    Page<BookEntity> getBookUpSale(Pageable pageable);

    @Query(value = "" +
            "SELECT b.* " +
            "FROM book b " +
            "JOIN category c ON b.category_id = c.id " +
            "WHERE c.category_name LIKE %:category%",
            countQuery = "SELECT COUNT(*) " +
                    "FROM book b " +
                    "JOIN category c ON b.category_id = c.id " +
                    "WHERE c.category_name LIKE %:category%",
            nativeQuery = true)
    Page<BookEntity> getBookByCategory(@Param("category") String category, Pageable pageable);

    @Query(value = "SELECT * FROM book WHERE average_rating > 4.5 and title LIKE %:keyword% ", nativeQuery = true)
    Page<BookEntity> getNewReleasedBooks(Pageable pageable, String keyword);

    @Query(value = "SELECT * FROM book WHERE title LIKE %:title%", nativeQuery = true)
    List<BookEntity> getBookByTitle(@Param("title") String title);


    @Query(value = "select count(id) as totalBook from book;",nativeQuery = true)
    int countBook();

    @Query(value = "SELECT * FROM book WHERE isbn = :isbn", nativeQuery = true)
    BookEntity findBookByIsbn(@Param("isbn") String isbn);

}
