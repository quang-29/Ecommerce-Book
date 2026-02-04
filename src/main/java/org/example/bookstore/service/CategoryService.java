package org.example.bookstore.service;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.CategoryEntity;
import org.example.bookstore.payload.CategoryDTO;
import org.example.bookstore.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.Long;



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
            throw new ResourceNotFoundException(MessageException.CATEGORY_ALREADY_EXISTS);
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(name);
        categoryRepository.save(categoryEntity);
        return ServerResponseDto.success("Add category successfully!");
    }

    public ServerResponseDto getCategoryById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CATEGORY_NOT_FOUND));
        return ServerResponseDto.success(modelMapper.map(categoryEntity, CategoryDTO.class));
    }


    public ServerResponseDto getAllCategories(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<CategoryDTO> categoryDTOPage = categoryRepository.findAll(pageable).map(categoryEntity -> modelMapper.map(categoryEntity, CategoryDTO.class));
        return ServerResponseDto.success(categoryDTOPage);
    }

    public ServerResponseDto updateCategory(Long id, CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CATEGORY_NOT_FOUND));
        categoryEntity.setName(categoryDTO.getCategoryName());

        categoryRepository.save(categoryEntity);
        return ServerResponseDto.success("Update category successfully");
    }

}
