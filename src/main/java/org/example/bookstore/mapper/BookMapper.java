package org.example.bookstore.mapper;

import org.example.bookstore.model.BookEntity;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.repository.AuthorRepository;
import org.example.bookstore.repository.CategoryRepository;
import org.example.bookstore.repository.StoreBookRepository;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final StoreBookRepository storeBookRepository;

    public BookMapper(AuthorRepository authorRepository, CategoryRepository categoryRepository, StoreBookRepository storeBookRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.storeBookRepository = storeBookRepository;
    }

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
        var storeBooks = storeBookRepository.findByBookId(bookEntity.getId());
        bookDTO.setStock(storeBooks.isEmpty()
                ? bookEntity.getStock()
                : storeBooks.stream()
                .mapToLong(storeBook -> storeBook.getStock() == null ? 0L : storeBook.getStock())
                .sum());
        bookDTO.setSold(bookEntity.getSold());
        bookDTO.setPage(bookEntity.getPage());
        bookDTO.setReprint(bookEntity.getReprint());
        bookDTO.setPublishedDate(String.valueOf(bookEntity.getPublishedDate()));
        bookDTO.setAverageRating(bookEntity.getAverageRating());
        if (bookEntity.getCategoryId() != null) {
            categoryRepository.findById(bookEntity.getCategoryId()).ifPresent(category -> bookDTO.setCategoryName(category.getName()));
        }
        if (bookEntity.getAuthorId() != null) {
            authorRepository.findById(bookEntity.getAuthorId()).ifPresent(author -> bookDTO.setAuthorName(author.getName()));
        }
        return bookDTO;

    }

}
