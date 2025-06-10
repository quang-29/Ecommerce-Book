package org.example.bookstore.repository;

import org.example.bookstore.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query(value = "SELECT * FROM book WHERE title COLLATE utf8mb4_bin LIKE CONCAT('%', :title, '%')", nativeQuery = true)
    Book findByName(String title);

    Page<Book> findByCategory_Name(String category, Pageable pageable);
    Page<Book> findByAuthor_Name(String authorName, Pageable pageable);

    @Query(value = "SELECT * FROM book  WHERE sold > 10", nativeQuery = true)
    Page<Book> getBookUpSale(Pageable pageable);

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
    Page<Book> getBookByCategory(@Param("category") String category, Pageable pageable);

    @Query(value = "SELECT * FROM book WHERE average_rating > 4.5", nativeQuery = true)
    Page<Book> getNewReleasedBooks(Pageable pageable);

    @Query(value = "SELECT * FROM book WHERE title LIKE %:title%", nativeQuery = true)
    List<Book> getBookByTitle(@Param("title") String title);


    @Query(value = "select count(id) as totalBook from book;",nativeQuery = true)
    int countBook();

    @Query(value = "SELECT * FROM book WHERE isbn = :isbn", nativeQuery = true)
    Book findBookByIsbn(@Param("isbn") String isbn);

}
