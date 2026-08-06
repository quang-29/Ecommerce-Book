package org.example.bookstore.controller;


import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.AuthorDTO;
import org.example.bookstore.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.lang.Long;

@RestController
@RequestMapping("/api/v1/author")
public class AuthorController {


    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/add")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> saveAuthor(@ModelAttribute AuthorDTO authorDTO) throws FileUploadException {
        return ResponseEntity.ok(authorService.saveAuthor(authorDTO));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> deleteAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.deleteAuthor(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));

    }

    @GetMapping("/all")
    public ResponseEntity<ServerResponseDto> getAllAuthors(@RequestParam(defaultValue = "0") int size,
                                                       @RequestParam(defaultValue = "10") int page,
                                                       @RequestParam(required = false) String sortBy,
                                                       @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(authorService.getAllAuthors(page, size, sortBy, sortDirection));
    }

    @GetMapping("/name/{authorName}")
    public ResponseEntity<ServerResponseDto> getAuthorByName(@RequestParam String authorName) {
        return ResponseEntity.ok(authorService.getAuthorByName(authorName));
    }

    @GetMapping("/get-page")
    public ResponseEntity<ServerResponseDto> getPage(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(required = false) String sortField,
                                                     @RequestParam(defaultValue = "desc") String sortDir,
                                                     @RequestParam String keywordSearch){
        return ResponseEntity.ok(authorService.getPageAuthor(page, size, sortField, sortDir, keywordSearch));

    }
}
