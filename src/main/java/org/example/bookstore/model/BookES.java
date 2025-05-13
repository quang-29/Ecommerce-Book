package org.example.bookstore.model;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@Document(indexName = "books")
public class BookES {
    @Id
    private Long id;
    private String title;
    private String author;
    private String description;
}
