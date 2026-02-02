package org.example.bookstore.service;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.AuthorEntity;
import org.example.bookstore.model.BookEntity;
import org.example.bookstore.model.CategoryEntity;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.request.CreateBookRequest;
import org.example.bookstore.payload.response.CloudinaryResponse;
import org.example.bookstore.repository.AuthorRepository;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.CategoryRepository;
import org.example.bookstore.utils.FileUploadUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.lang.Long;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class BookService {

    private final CategoryRepository categoryRepository;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(CategoryRepository categoryRepository, CloudinaryServiceImpl cloudinaryServiceImpl, ModelMapper modelMapper, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.categoryRepository = categoryRepository;
        this.cloudinaryServiceImpl = cloudinaryServiceImpl;
        this.modelMapper = modelMapper;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public ServerResponseDto getBookById(Long id) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageException.AUTHOR_NOT_FOUND.getMessage()));
        return ServerResponseDto.success( modelMapper.map(bookEntity, BookDTO.class));
    }

    public ServerResponseDto getAllBooks(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<BookDTO> pageBooks = bookRepository.findAll(pageable).map(book -> modelMapper.map(book, BookDTO.class));
        return ServerResponseDto.success(pageBooks);
    }

    public ServerResponseDto getAllBooksByAuthor(String authorName, int page, int size, String sortBy, String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageDetails = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<BookDTO> pageBooks = bookRepository.findByAuthor_Name(authorName, pageDetails).map(book -> modelMapper.map(book, BookDTO.class));
        return ServerResponseDto.success(pageBooks);
    }

    public ServerResponseDto getAllBooksByCategory(String category, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Pageable pageDetails = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<BookDTO> pageBooks = bookRepository.findByCategory_Name(category, pageDetails).map(book -> modelMapper.map(book, BookDTO.class));
        return ServerResponseDto.success(pageBooks);
    }

    @Transactional
    public ServerResponseDto addBook(CreateBookRequest request) {
        BookEntity foundBookEntity = bookRepository.findByName(request.getTitle());
        if(foundBookEntity != null) {
            throw new RuntimeException(MessageException.BOOK_EXIST.getMessage());
        }
        BookEntity bookEntity = modelMapper.map(request, BookEntity.class);
        bookEntity.setSold(0L);
        bookEntity.setPublishedDate(LocalDate.parse(request.getPublishedDate()));
        Optional<CategoryEntity> optionalCategory = categoryRepository.findByName(request.getCategory());
        if(optionalCategory.isPresent()){
            bookEntity.setCategoryEntity(optionalCategory.get());
        }
        Optional<AuthorEntity> optionalAuthor = authorRepository.findByName(request.getAuthor());
        if(optionalAuthor.isPresent()){
            bookEntity.setAuthorEntity(optionalAuthor.get());
        } else {
            AuthorEntity authorEntity = new AuthorEntity();
            authorEntity.setName(request.getAuthor());
            authorRepository.save(authorEntity);
            bookEntity.setAuthorEntity(authorEntity);
        }
        BookEntity bookEntitySaved = bookRepository.save(bookEntity);
        BookDTO bookDTO = modelMapper.map(bookEntitySaved, BookDTO.class);
        bookDTO.setAuthorName(bookEntitySaved.getAuthorEntity().getName());
        return ServerResponseDto.success(bookDTO);

    }

    @Transactional
    public ServerResponseDto uploadImageBook(Long id, MultipartFile file) {
            try {
                Optional<BookEntity> optionalBook = bookRepository.findById(id);
                if(optionalBook.isEmpty()){
                    throw new AppException(ErrorCode.BOOK_NOT_FOUND);
                }
                BookEntity bookEntity = optionalBook.get();
                FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
                final String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
                final CloudinaryResponse response = cloudinaryServiceImpl.uploadFile(file, fileName);
                bookEntity.setImagePath(response.getUrl());
                bookRepository.save(bookEntity);
                return ServerResponseDto.success(response);
            } catch (Exception ex){
                throw new RuntimeException(ex.getMessage());
            }
        }

    public ServerResponseDto updateBook(Long id, BookDTO bookDTO) {
        BookEntity bookEntityFound = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageException.BOOK_NOT_FOUND.getMessage()));
        modelMapper.map(bookDTO, bookEntityFound);
        AuthorEntity authorEntity = authorRepository.findByName(bookDTO.getAuthorName())
                .orElseThrow(() -> new RuntimeException(MessageException.AUTHOR_NOT_FOUND.getMessage()));
        bookEntityFound.setAuthorEntity(authorEntity);
        CategoryEntity categoryEntity = categoryRepository.findByName(bookDTO.getCategoryName())
                .orElseGet(() -> {
                    CategoryEntity newCategoryEntity = new CategoryEntity();
                    newCategoryEntity.setName(bookDTO.getCategoryName());
                    return categoryRepository.save(newCategoryEntity);
                });
        bookEntityFound.setCategoryEntity(categoryEntity);
        BookEntity savedBookEntity = bookRepository.save(bookEntityFound);
        BookDTO updatedBookDTO = modelMapper.map(savedBookEntity, BookDTO.class);
        updatedBookDTO.setAuthorName(savedBookEntity.getAuthorEntity().getName());
        updatedBookDTO.setCategoryName(savedBookEntity.getCategoryEntity().getName());
        return ServerResponseDto.success(updatedBookDTO);
    }

    public ServerResponseDto deleteBook(Long id) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book","bookId", id));
        bookRepository.delete(bookEntity);
        return ServerResponseDto.success("Delete book Successfully");
    }

    public ServerResponseDto getBookUpSale(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<BookDTO> pageBooks = bookRepository.getBookUpSale(pageable).map(book -> modelMapper.map(book, BookDTO.class));
        return ServerResponseDto.success(pageBooks);
    }

    public ServerResponseDto getNewReleaseBook(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<BookDTO> pageBooks = bookRepository.getNewReleasedBooks(pageDetails).map(book -> modelMapper.map(book, BookDTO.class));
        return ServerResponseDto.success(pageBooks);
    }

    public ServerResponseDto getBookByTitle(String title) {
        List<BookEntity> bookEntityList = bookRepository.getBookByTitle(title);

        List<BookDTO> bookDTOList = bookEntityList.stream().map(book -> modelMapper.map(book, BookDTO.class)).toList();
        return ServerResponseDto.success(bookDTOList) ;
    }

    public ServerResponseDto getBookByISBN(String isbn) {
        BookEntity bookEntity = bookRepository.findBookByIsbn(isbn);
        if(bookEntity == null) {
            throw new RuntimeException(MessageException.BOOK_NOT_FOUND.getMessage());
        }

        return ServerResponseDto.success(modelMapper.map(bookEntity, BookDTO.class));
    }

    public ServerResponseDto searchBookByContent(String content) {
        String cleanText = normalizeText(content);
        List<BookEntity> allBookEntities = bookRepository.findAll();

        BookEntity bestMatch = null;
        double highestScore = 0.0;

        for (BookEntity bookEntity : allBookEntities) {
            String combined = (bookEntity.getTitle() + " " + bookEntity.getAuthorEntity() + " " + bookEntity.getDescription()).toLowerCase();
            double score = similarity(cleanText, combined);
            if (score > highestScore) {
                highestScore = score;
                bestMatch = bookEntity;
            }
        }

        if (bestMatch == null) {
            throw new NoSuchElementException("Không tìm thấy sách ");
        }

        if (bestMatch != null && highestScore >= 0.5) {
            return ServerResponseDto.success(modelMapper.map(bestMatch, BookDTO.class));
        }

        return null;
    }

    private String normalizeText(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private double similarity(String s1, String s2) {
        int distance = levenshteinDistance(s1, s2);
        int maxLen = Math.max(s1.length(), s2.length());
        return (maxLen == 0) ? 1.0 : 1.0 - (double) distance / maxLen;
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,
                                dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[s1.length()][s2.length()];
    }

}
