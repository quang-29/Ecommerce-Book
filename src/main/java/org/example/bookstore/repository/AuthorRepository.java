package org.example.bookstore.repository;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.bookstore.model.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    boolean existsByName(String name);

    Optional<AuthorEntity> findByName(@Size(min = 3, max = 20, message = "Author Name must be between 5 and 20 characters long") @Pattern(regexp = "^[a-zA-Z]*$", message = "Author Name must not contain numbers or special characters") String name);

    @Query(value = "SELECT * FROM author WHERE name LIKE CONCAT('%', :keyword, '%')",
            countQuery = "SELECT COUNT(*) FROM author WHERE name LIKE CONCAT('%', :keyword, '%')",
            nativeQuery = true)
    Page<AuthorEntity> getPageAuthor(@Param("keyword") String keyword, Pageable pageable);
}
