package org.example.bookstore.payload;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorDTO {
    private String id;
    private String name;
    private String biography;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String country;
    private String website;
    private MultipartFile avatarUrl;
}
