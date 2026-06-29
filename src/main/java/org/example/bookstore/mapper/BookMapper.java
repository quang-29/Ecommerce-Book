package org.example.bookstore.mapper;

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
        bookDTO.setStock(bookEntity.getStoreBooks() == null || bookEntity.getStoreBooks().isEmpty()
                ? bookEntity.getStock()
                : bookEntity.getStoreBooks().stream()
                .mapToLong(storeBook -> storeBook.getStock() == null ? 0L : storeBook.getStock())
                .sum());
        bookDTO.setSold(bookEntity.getSold());
        bookDTO.setPage(bookEntity.getPage());
        bookDTO.setReprint(bookEntity.getReprint());
        bookDTO.setPublishedDate(String.valueOf(bookEntity.getPublishedDate()));
        bookDTO.setAverageRating(bookEntity.getAverageRating());
        if (bookEntity.getCategoryEntity() != null) {
            bookDTO.setCategoryName(bookEntity.getCategoryEntity().getName());
        }
        if (bookEntity.getAuthorEntity() != null) {
            bookDTO.setAuthorName(bookEntity.getAuthorEntity().getName());
        }
        return bookDTO;

    }

}
