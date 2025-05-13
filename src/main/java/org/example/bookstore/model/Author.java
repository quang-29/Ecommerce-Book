package org.example.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(min = 3, max = 20, message = "Author Name must be between 5 and 20 characters long")
    @Column(name = "author_name")
    private String name;

    @Column(name = "biography")
    private String biography;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "birth_date")
    private Date birth_date;

    @Column(name = "country")
    private String country;

    @Column(name = "image_path")
    private String image_path;

    @OneToMany(mappedBy = "author")
    private Set<Book> books;

}