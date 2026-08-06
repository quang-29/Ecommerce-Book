package org.example.bookstore.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.AuthorEntity;
import org.example.bookstore.payload.AuthorDTO;
import org.example.bookstore.repository.AuthorRepository;
import org.example.bookstore.utils.FileUploadUtil;
import org.example.bookstore.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.lang.Long;
import java.util.Optional;

@Service
@Slf4j
public class AuthorService {

    private static final String AUTHOR_IMAGE_SUB_BUCKET = "author";

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    public AuthorService(AuthorRepository authorRepository, ModelMapper modelMapper, FileService fileService) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    public ServerResponseDto saveAuthor(AuthorDTO authorDTO) throws FileUploadException {
        String authorId = authorDTO.getId();
        if (StringUtils.isNullOrEmpty(authorId)) {
             return createAuthor(authorDTO);
        } else {
            return updateAuthor(authorDTO);
        }
    }

    public ServerResponseDto createAuthor(AuthorDTO authorDTO) throws FileUploadException {
        if (authorRepository.existsByName(authorDTO.getName())) {
            throw new ResourceNotFoundException(MessageException.AUTHOR_EXISTED);
        }
        AuthorEntity authorEntity = modelMapper.map(authorDTO, AuthorEntity.class);
        uploadAvatar(authorDTO.getAvatarUrl()).ifPresent(authorEntity::setImageUrl);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        return ServerResponseDto.success(modelMapper.map(savedAuthorEntity, AuthorDTO.class));
    }

    public ServerResponseDto updateAuthor(AuthorDTO authorDTO) throws FileUploadException {
        AuthorEntity authorEntity = authorRepository.findById(parseAuthorId(authorDTO.getId())).orElseThrow(
                () -> new ResourceNotFoundException(MessageException.AUTHOR_NOT_FOUND)
        );
        authorEntity.mapToAuthorEntity(authorDTO);
        uploadAvatar(authorDTO.getAvatarUrl()).ifPresent(authorEntity::setImageUrl);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        return ServerResponseDto.success(modelMapper.map(savedAuthorEntity, AuthorDTO.class));
    }

    private Long parseAuthorId(String id) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException ex) {
            throw new ResourceNotFoundException(MessageException.AUTHOR_NOT_FOUND);
        }
    }

    private Optional<String> uploadAvatar(MultipartFile avatarFile) throws FileUploadException {
        if (avatarFile == null || avatarFile.isEmpty()) {
            return Optional.empty();
        }
        FileUploadUtil.assertAllowed(avatarFile, FileUploadUtil.IMAGE_PATTERN);
        return fileService.uploadFile(AUTHOR_IMAGE_SUB_BUCKET, avatarFile);
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
    private String mapSortColumn(String sortField) {
        if ("imageUrl".equalsIgnoreCase(sortField)) {
            return "image_url";
        }
        return sortField;
    }

    public ServerResponseDto getPageAuthor(int page, int size, String sortField, String sortDir, String keywordSearch){
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mapSortColumn(sortField)));
        log.info("Fetching list author - keyword: '{}', page: {}, size: {}, sort: {}", keywordSearch, page, size, sortField);

        Page<AuthorDTO> authorDTOPage = authorRepository.getPageAuthor(keywordSearch, pageable)
                .map(authorEntity -> modelMapper.map(authorEntity, AuthorDTO.class));
        return ServerResponseDto.success(authorDTOPage);
    }
}
