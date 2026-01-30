package org.example.bookstore.service;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.Category;
import org.example.bookstore.payload.CategoryDTO;
import org.example.bookstore.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public ServerResponseDto addCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new RuntimeException(MessageException.CATEGORY_ALREADY_EXISTS.getMessage());
        }
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return ServerResponseDto.success("Add category successfully!");
    }

    public ServerResponseDto getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return ServerResponseDto.success(modelMapper.map(category, CategoryDTO.class));
    }


    public ServerResponseDto getAllCategories(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<CategoryDTO> categoryDTOPage = categoryRepository.findAll(pageable).map(category -> modelMapper.map(category, CategoryDTO.class));
        return ServerResponseDto.success(categoryDTOPage);
    }

    public ServerResponseDto updateCategory(UUID id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        category.setName(categoryDTO.getCategoryName());

        categoryRepository.save(category);
        return ServerResponseDto.success("Update category successfully");
    }

}
