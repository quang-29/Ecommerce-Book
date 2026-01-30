package org.example.bookstore.controller;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.request.CreateBookRequest;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;

    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServerResponseDto> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping("/addBook")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> addBook(@RequestBody CreateBookRequest request) {
        return ResponseEntity.ok(bookService.addBook(request));
    }

    @PostMapping("/uploadImageBook/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> uploadImageBook(
            @PathVariable UUID bookId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(bookService.uploadImageBook(bookId,file));
    }

    @GetMapping("/books")
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

    @PutMapping("book/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> updateBook(@PathVariable UUID id, @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok( bookService.updateBook(id, bookDTO));
    }


    @DeleteMapping("deleteBook/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> deleteBook(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.deleteBook(id));
    }

    @GetMapping("/upSaleBook")
    public ResponseEntity<ServerResponseDto> upSaleBook(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(required = false) String sortBy,
                                                        @RequestParam(required = false) String sortDirection
    ){
        return ResponseEntity.ok(bookService.getBookUpSale(page, size, sortBy, sortDirection));
    }

    @GetMapping("/getNewReleaseBook")
    public ResponseEntity<ServerResponseDto> getNewReleaseBook(@RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer size,
                                                                @RequestParam(required = false) String sortBy,
                                                                @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(bookService.getNewReleaseBook(page, size, sortBy, sortDirection));
    }

    @GetMapping("/search")
    public ResponseEntity<ServerResponseDto> getBookByName(@RequestParam String name) {
        return ResponseEntity.ok(bookService.getBookByTitle(name));
    }

    @GetMapping("/searchByISBN")
    public ResponseEntity<ServerResponseDto> searchByISBN(@RequestParam String isbn) {
        return ResponseEntity.ok(bookService.getBookByISBN(isbn));
    }

    @GetMapping("/getNumberOfBooks")
    public ResponseEntity<?> getNumberOfBooks(){
        int number = bookRepository.countBook();
        return ResponseEntity.ok(number);
    }

    @GetMapping("/searchByContent")
    public ResponseEntity<ServerResponseDto> searchBookByContent(@RequestParam String text) {
        return ResponseEntity.ok(bookService.searchBookByContent(text));
    }

}
