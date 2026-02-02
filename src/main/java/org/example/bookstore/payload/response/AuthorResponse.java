package org.example.bookstore.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstore.model.BookEntity;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {
    private Long id;
    private String name;
    private String bio;
    private String email;
    private String address;
    private String phone;
    private Set<BookEntity> bookEntities;
}
