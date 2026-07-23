package org.example.bookstore.service;

import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.enums.SortDir;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.AuthorEntity;
import org.example.bookstore.payload.AuthorDTO;
import org.example.bookstore.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.Long;

@Service
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    public AuthorService(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    public ServerResponseDto createAuthor(AuthorDTO authorDTO) {
        boolean existedAuthor = authorRepository.existsByName(authorDTO.getName());
        if(existedAuthor){
            throw new ResourceNotFoundException(MessageException.AUTHOR_EXISTED);
        }
        AuthorEntity authorEntity = modelMapper.map(authorDTO, AuthorEntity.class);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        return ServerResponseDto.success(modelMapper.map(savedAuthorEntity, AuthorDTO.class));

    }

    public ServerResponseDto updateAuthor(Long id, AuthorDTO authorDTO) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageException.AUTHOR_NOT_FOUND)
        );
        authorEntity.mapToAuthorEntity(authorDTO);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        return ServerResponseDto.success(modelMapper.map(savedAuthorEntity, AuthorDTO.class));
    }

    public ServerResponseDto deleteAuthor(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(MessageException.AUTHOR_NOT_FOUND));
        authorRepository.delete(authorEntity);
        return ServerResponseDto.SUCCESS;
    }

    public ServerResponseDto getAuthorById(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(MessageException.AUTHOR_NOT_FOUND));
        return ServerResponseDto.success(modelMapper.map(authorEntity, AuthorDTO.class));
    }

    public ServerResponseDto getAllAuthors(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<AuthorDTO> authorDTOPage = authorRepository.findAll(pageable)
                .map(authorEntity -> modelMapper.map(authorEntity, AuthorDTO.class));
        return ServerResponseDto.success(authorDTOPage);
    }

    public ServerResponseDto getAuthorByName(String authorName) {
        AuthorEntity authorEntity = authorRepository.findByName(authorName)
                .orElseThrow(()-> new ResourceNotFoundException(MessageException.AUTHOR_NOT_FOUND));
        return ServerResponseDto.success(modelMapper.map(authorEntity, AuthorDTO.class));
    }
    public ServerResponseDto getPageAuthor(int page, int size, String sortField, String sortDir, String keywordSearch){
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        log.info("Fetching list author - keyword: '{}', page: {}, size: {}, sort: {}", keywordSearch, page, size, sortField);

        Page<AuthorDTO> authorDTOPage = authorRepository.getPageAuthor(keywordSearch, pageable)
                .map(authorEntity -> modelMapper.map(authorEntity, AuthorDTO.class));
        return ServerResponseDto.success(authorDTOPage);
    }
}
