package org.example.bookstore.service;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.Author;
import org.example.bookstore.payload.AuthorDTO;
import org.example.bookstore.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.UUID;
@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    private final ModelMapper modelMapper;

    public AuthorService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ServerResponseDto createAuthor(AuthorDTO authorDTO) {
        boolean existedAuthor = authorRepository.existsByName(authorDTO.getName());
        if(existedAuthor){
            throw new AppException(ErrorCode.AUTHOR_EXISTED);
        }
        Author author = modelMapper.map(authorDTO, Author.class);
        Author savedAuthor = authorRepository.save(author);
        return ServerResponseDto.success(modelMapper.map(savedAuthor, AuthorDTO.class));

    }

    public ServerResponseDto updateAuthor(UUID id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id).orElseThrow(
                () -> new RuntimeException(MessageException.AUTHOR_NOT_FOUND.getMessage())
        );
        author.setName(authorDTO.getName());
        author.setBiography(authorDTO.getBiography());
        author.setEmail(authorDTO.getEmail());
        author.setCountry(authorDTO.getCountry());
        author.setWebsite(authorDTO.getWebsite());
        author.setImage_path(authorDTO.getImage_path());
        author.setBirth_date(authorDTO.getBirth_date());
        Author savedAuthor = authorRepository.save(author);
        return ServerResponseDto.success(modelMapper.map(savedAuthor, AuthorDTO.class));
    }

    public ServerResponseDto deleteAuthor(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(()-> new RuntimeException(MessageException.AUTHOR_NOT_FOUND.getMessage()));
        authorRepository.delete(author);
        return ServerResponseDto.success("Delete author successfully!");
    }

    public ServerResponseDto getAuthorById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(()-> new RuntimeException(MessageException.AUTHOR_NOT_FOUND.getMessage()));
        return ServerResponseDto.success(modelMapper.map(author, AuthorDTO.class));
    }

    public ServerResponseDto getAllAuthors(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Author> authorPage = authorRepository.findAll(pageable);
        return ServerResponseDto.success(authorPage);
    }

    public ServerResponseDto getAuthorByName(String authorName) {
        Author author = authorRepository.findByName(authorName)
                .orElseThrow(()-> new RuntimeException(MessageException.AUTHOR_NOT_FOUND.getMessage()));
        return ServerResponseDto.success(modelMapper.map(author, AuthorDTO.class));
    }
}
