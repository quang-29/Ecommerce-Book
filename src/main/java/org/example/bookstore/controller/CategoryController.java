package org.example.bookstore.controller;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.CategoryDTO;

import org.example.bookstore.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.Long;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> addCategory(@RequestParam String name) {
        return ResponseEntity.ok(ServerResponseDto.success(categoryService.addCategory(name)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServerResponseDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(ServerResponseDto.success(categoryService.getCategoryById(id)));
    }

    @GetMapping("/all")
    public ResponseEntity<ServerResponseDto> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(required = false) String sortBy,
                                                          @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(categoryService.getAllCategories(page, size, sortBy, sortDirection));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto>  updateCategory(@PathVariable Long id,
                                                         @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

}
