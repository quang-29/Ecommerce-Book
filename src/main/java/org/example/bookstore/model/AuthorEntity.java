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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
public class AuthorEntity extends BaseEntity {

    @Size(min = 3, max = 20, message = "Author Name must be between 5 and 20 characters long")
    private String name;

    private String biography;

    @Email
    private String email;
    private String website;
    private Date dob;
    private String country;
    private String imageUrl;

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
