package org.example.bookstore.payload;

import lombok.*;

import java.util.Date;

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
    private Date dob;
    private String country;
    private String website;
    private String imageUrl;
}
