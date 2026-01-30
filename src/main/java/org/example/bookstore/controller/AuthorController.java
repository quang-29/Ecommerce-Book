package org.example.bookstore.controller;


import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.AuthorDTO;
import org.example.bookstore.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping("/addAuthor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> addAuthor(@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.createAuthor(authorDTO));
    }

    @PutMapping("/updateAuthor/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> editAuthor(@PathVariable UUID id, @RequestBody AuthorDTO newAuthorDTO) {
        return ResponseEntity.ok(authorService.updateAuthor(id, newAuthorDTO));
    }

    @DeleteMapping("/deleteAuthor/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> deleteAuthor(@PathVariable UUID id) {
        return ResponseEntity.ok(authorService.deleteAuthor(id));
    }

    @GetMapping("/getAuthorById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> getAuthorById(@PathVariable UUID id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));

    }

    @GetMapping("/getAllAuthors")
    public ResponseEntity<ServerResponseDto> getAllAuthors(@RequestParam(defaultValue = "0") int size,
                                                      @RequestParam(defaultValue = "10") int page,
                                                      @RequestParam(required = false) String sortBy,
                                                      @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(authorService.getAllAuthors(page, size, sortBy, sortDirection));
    }

    @GetMapping("/getAuthorByName/{authorName}")
    public ResponseEntity<ServerResponseDto> getAuthorByName(@RequestParam String authorName) {
        return ResponseEntity.ok(authorService.getAuthorByName(authorName));
    }
}
