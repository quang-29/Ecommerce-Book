package org.example.bookstore.payload;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.bookstore.model.Book;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorDTO {
    private UUID id;
    private String name;
    private String biography;
    private String email;
    private Date birth_date;
    private String country;
    private String website;
    private String image_path;
}
