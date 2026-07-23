package org.example.bookstore.repository;

import org.example.bookstore.model.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);

    boolean existsByName(String name);

    @Query(value = "" +
            "SELECT *" +
            "FROM category " +
            "WHERE category_name LIKE CONCAT('%', :keywordSearch, '%')",
            countQuery = "" +
                    "SELECT COUNT(*)" +
                    "FROM category " +
                    "WHERE category_name LIKE CONCAT('%', :keywordSearch, '%')",
            nativeQuery = true)
    Page<CategoryEntity> getPageCategory(@Param("keywordSearch") String keywordSearch, Pageable pageable);
}
