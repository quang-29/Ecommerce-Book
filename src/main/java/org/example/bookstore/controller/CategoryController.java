package org.example.bookstore.controller;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.CategoryDTO;

import org.example.bookstore.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> addCategory(@RequestParam String name) {
        return ResponseEntity.ok(ServerResponseDto.success(categoryService.addCategory(name)));
    }

    @GetMapping("/getCategoryById/{id}")
    public ResponseEntity<ServerResponseDto> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(ServerResponseDto.success(categoryService.getCategoryById(id)));
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<ServerResponseDto> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(required = false) String sortBy,
                                                         @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(categoryService.getAllCategories(page, size, sortBy, sortDirection));
    }

    @PutMapping("/updateCategory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto>  updateCategory(@PathVariable UUID id,
                                                             @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

}
