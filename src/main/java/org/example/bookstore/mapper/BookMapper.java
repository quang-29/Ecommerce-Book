package org.example.bookstore.mapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.BookES;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class BookMapper {

    public BookES toBookES(Book book) {
        BookES bookES = new BookES();
        bookES.setTitle(book.getTitle());
        bookES.setDescription(book.getDescription());
        bookES.setAuthor(book.getAuthor().getName());
        return bookES;
    }
}
