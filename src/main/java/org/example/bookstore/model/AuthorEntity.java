package org.example.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstore.payload.AuthorDTO;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
public class AuthorEntity extends BaseEntity {

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

    @Column(name = "dob")
    private Date dob;

    @Column(name = "country")
    private String country;

    @Column(name = "imageUrl")
    private String imageUrl;

    @OneToMany(mappedBy = "author")
    private Set<BookEntity> bookEntities;
    
    public void mapToAuthorEntity(AuthorDTO authorDTO){
        this.name = authorDTO.getName();
        this.biography = authorDTO.getBiography();
        this.email = authorDTO.getEmail();
        this.country = authorDTO.getCountry();
        this.website = authorDTO.getWebsite();
        this.imageUrl = authorDTO.getImageUrl();
        this.dob = authorDTO.getDob();
    }

}