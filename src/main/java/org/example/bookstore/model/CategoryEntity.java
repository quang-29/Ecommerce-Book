package org.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity {

    @Column(name = "category_name")
    private String name;

    @Column(name = "category_img")
    private String category_img;

    @OneToMany(mappedBy = "categoryEntity")
    private Set<BookEntity> bookEntities;

}