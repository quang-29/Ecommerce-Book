package org.example.bookstore.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Note {
    private String context;
    private String subject;
    private String content;

}
