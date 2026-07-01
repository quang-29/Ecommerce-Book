package org.example.bookstore.controller;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.request.CreateBookRequest;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.Long;


@RestController
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;

    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServerResponseDto> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping("/add")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> addBook(@RequestBody CreateBookRequest request) {
        return ResponseEntity.ok(bookService.addBook(request));
    }

    @PostMapping("/uploadImage/{bookId}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> uploadImageBook(
            @PathVariable Long bookId,
            @RequestParam("file") MultipartFile file) throws FileUploadException {
        return ResponseEntity.ok(bookService.uploadImageBook(bookId,file));
    }

    @GetMapping("/all")
    public ResponseEntity<ServerResponseDto> getAllBooks(@RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "20") Integer size,
                                                     @RequestParam(required = false) String sortBy,
                                                     @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(bookService.getAllBooks(page, size, sortBy, sortDirection));
    }


    @GetMapping("/books/{authorName}")
    public ResponseEntity<ServerResponseDto> getBooksByAuthor(@PathVariable String authorName,
                                                          @RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer size,
                                                          @RequestParam(required = false) String sortBy,
                                                          @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(bookService.getAllBooksByAuthor(authorName, page, size, sortBy, sortDirection));
    }


    @GetMapping("/books/{category}")
    public ResponseEntity<ServerResponseDto> getBooksByCategory(@PathVariable String category,
                                                            @RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer size,
                                                            @RequestParam(required = false) String sortBy,
                                                            @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(bookService.getAllBooksByCategory(category, page, size, sortBy, sortDirection));
    }

    @PutMapping("/book/{id}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok( bookService.updateBook(id, bookDTO));
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> deleteBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.deleteBook(id));
    }

    @GetMapping("/get-upsale-books")
    public ResponseEntity<ServerResponseDto> upSaleBook(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(required = false) String sortBy,
                                                    @RequestParam(required = false) String sortDirection
    ){
        return ResponseEntity.ok(bookService.getBookUpSale(page, size, sortBy, sortDirection));
    }

    @GetMapping("/get-new-release-books")
    public ResponseEntity<ServerResponseDto> getNewReleaseBook(@RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "10") Integer size,
                                                               @RequestParam(required = false) String sortBy,
                                                               @RequestParam(required = false) String sortDirection,
                                                               @RequestParam(required = false) String keyword, Sort sort) {
        Page<BookDTO> bookDTOPage = bookService.getNewReleaseBook(page, size, sortBy, sortDirection, keyword);
        return ResponseEntity.ok(ServerResponseDto.success(bookDTOPage));
    }

    @GetMapping("/search")
    public ResponseEntity<ServerResponseDto> getBookByName(@RequestParam String name) {
        return ResponseEntity.ok(bookService.getBookByTitle(name));
    }

    @GetMapping("/search-by-ISBN")
    public ResponseEntity<ServerResponseDto> searchByISBN(@RequestParam String isbn) {
        return ResponseEntity.ok(bookService.getBookByISBN(isbn));
    }

    @GetMapping("/get-number-of-books")
    public ResponseEntity<?> getNumberOfBooks(){
        int number = bookRepository.countBook();
        return ResponseEntity.ok(number);
    }

    @GetMapping("/search-by-content")
    public ResponseEntity<ServerResponseDto> searchBookByContent(@RequestParam String text) {
        return ResponseEntity.ok(bookService.searchBookByContent(text));
    }

}
