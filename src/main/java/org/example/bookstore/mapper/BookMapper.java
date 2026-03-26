package org.example.bookstore.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.example.bookstore.model.BookEntity;
import org.example.bookstore.payload.BookDTO;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class BookMapper {
    public BookDTO mapBookEntityToDto(BookEntity bookEntity){
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(bookEntity.getId());
        bookDTO.setTitle(bookEntity.getTitle());
        bookDTO.setDescription(bookEntity.getDescription());
        bookDTO.setPrice(bookEntity.getPrice());
        bookDTO.setPublisher(bookEntity.getPublisher());
        bookDTO.setIsbn(bookEntity.getIsbn());
        bookDTO.setLanguage(bookEntity.getLanguage());
        bookDTO.setImagePath(bookEntity.getImagePath());
        bookDTO.setStock(bookEntity.getStock());
        bookDTO.setSold(bookEntity.getSold());
        bookDTO.setPage(bookEntity.getPage());
        bookDTO.setReprint(bookEntity.getReprint());
        bookDTO.setPublishedDate(String.valueOf(bookEntity.getPublishedDate()));
        bookDTO.setAverageRating(bookEntity.getAverageRating());
        bookDTO.setCategoryName(bookEntity.getCategoryEntity().getName());
        bookDTO.setAuthorName(bookEntity.getAuthorEntity().getName());

    }

}
