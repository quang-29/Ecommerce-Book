package org.example.bookstore.payload;

import lombok.*;

import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorDTO {
    private Long id;
    private String name;
    private String biography;
    private String email;
    private Date birth_date;
    private String country;
    private String website;
    private String image_path;
}
