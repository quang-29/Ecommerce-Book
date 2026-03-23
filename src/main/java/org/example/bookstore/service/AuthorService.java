package org.example.bookstore.service;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.enums.ResponseCase;
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
        authorEntity.setName(authorDTO.getName());
        authorEntity.setBiography(authorDTO.getBiography());
        authorEntity.setEmail(authorDTO.getEmail());
        authorEntity.setCountry(authorDTO.getCountry());
        authorEntity.setWebsite(authorDTO.getWebsite());
        authorEntity.setImage_path(authorDTO.getImage_path());
        authorEntity.setBirth_date(authorDTO.getBirth_date());
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
        Page<AuthorEntity> authorPage = authorRepository.findAll(pageable);
        return ServerResponseDto.success(authorPage);
    }

    public ServerResponseDto getAuthorByName(String authorName) {
        AuthorEntity authorEntity = authorRepository.findByName(authorName)
                .orElseThrow(()-> new ResourceNotFoundException(MessageException.AUTHOR_NOT_FOUND));
        return ServerResponseDto.success(modelMapper.map(authorEntity, AuthorDTO.class));
    }
}
