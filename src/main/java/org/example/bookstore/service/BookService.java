package org.example.bookstore.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
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

    private static final String DEFAULT_SORT_BY = "id";
    private static final String DEFAULT_SORT_DIRECTION = "ASC";
    private static final double SEARCH_MATCH_THRESHOLD = 0.5;

    public BookService(CategoryRepository categoryRepository, CloudinaryServiceImpl cloudinaryServiceImpl, ModelMapper modelMapper, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.categoryRepository = categoryRepository;
        this.cloudinaryServiceImpl = cloudinaryServiceImpl;
        this.modelMapper = modelMapper;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public ServerResponseDto getBookById(Long id) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        return ServerResponseDto.success(mapToBookDto(bookEntity));
    }

    public ServerResponseDto getAllBooks(int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        Page<BookDTO> pageBooks = bookRepository.findAll(pageable).map(this::mapToBookDto);
        return ServerResponseDto.success(pageBooks);
    }

    public ServerResponseDto getPageBook(String keywordSearch, int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<BookEntity> pageBooks = bookRepository.getPageBook(keywordSearch, pageable);
        return ServerResponseDto.success(pageBooks);
    }


    @Transactional
    public ServerResponseDto addBook(CreateBookRequest request) {
        BookEntity foundBookEntity = bookRepository.findAllByTitle(request.getTitle());
        if(foundBookEntity != null) {
            throw new ResourceNotFoundException(MessageException.BOOK_EXIST);
        }
        BookEntity bookEntity = modelMapper.map(request, BookEntity.class);
        bookEntity.setSold(0L);
        bookEntity.setDiscountPercent(clampDiscountPercent(request.getDiscountPercent()));
        bookEntity.setPublishedDate(LocalDate.parse(request.getPublishedDate()));
        CategoryEntity categoryEntity = categoryRepository.findByName(request.getCategory())
                .orElseGet(() -> {
                    CategoryEntity newCategoryEntity = new CategoryEntity();
                    newCategoryEntity.setName(request.getCategory());
                    return categoryRepository.save(newCategoryEntity);
                });
        bookEntity.setCategoryEntity(categoryEntity);
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
        return ServerResponseDto.success(mapToBookDto(bookEntitySaved));

    }

    @Transactional
    public ServerResponseDto uploadImageBook(Long id, MultipartFile file) throws FileUploadException {
            try {
                Optional<BookEntity> optionalBook = bookRepository.findById(id);
                if(optionalBook.isEmpty()){
                    throw new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND);
                }
                BookEntity bookEntity = optionalBook.get();
                FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
                final String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
                final CloudinaryResponse response = cloudinaryServiceImpl.uploadFile(file, fileName);
                bookEntity.setImagePath(response.getUrl());
                bookRepository.save(bookEntity);
                return ServerResponseDto.success(response);
            } catch (FileUploadException ex){
                throw new FileUploadException(ex.getMessage());
            }
        }

    public ServerResponseDto updateBook(Long id, BookDTO bookDTO) {
        BookEntity bookEntityFound = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        modelMapper.map(bookDTO, bookEntityFound);
        bookEntityFound.setDiscountPercent(clampDiscountPercent(bookDTO.getDiscountPercent()));
        AuthorEntity authorEntity = authorRepository.findByName(bookDTO.getAuthorName())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.AUTHOR_NOT_FOUND));
        bookEntityFound.setAuthorEntity(authorEntity);
        CategoryEntity categoryEntity = categoryRepository.findByName(bookDTO.getCategoryName())
                .orElseGet(() -> {
                    CategoryEntity newCategoryEntity = new CategoryEntity();
                    newCategoryEntity.setName(bookDTO.getCategoryName());
                    return categoryRepository.save(newCategoryEntity);
                });
        bookEntityFound.setCategoryEntity(categoryEntity);
        BookEntity savedBookEntity = bookRepository.save(bookEntityFound);
        return ServerResponseDto.success(mapToBookDto(savedBookEntity));
    }

    public ServerResponseDto deleteBook(Long id) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        bookRepository.delete(bookEntity);
        return ServerResponseDto.success("Delete book Successfully");
    }

    public ServerResponseDto getBookUpSale(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, sortDirection);
        Page<BookDTO> pageBooks = bookRepository.getBookUpSale(pageable).map(this::mapToBookDto);
        return ServerResponseDto.success(pageBooks);
    }

    public Page<BookDTO> getNewReleaseBook(int pageNumber, int pageSize, String sortBy, String sortDirection, String keyword) {

        Page<BookEntity> listBooks = findBookBy(keyword, pageNumber, pageSize, sortBy, sortDirection);
        return listBooks.map(book -> mapToBookDto(book));
    }

    public Page<BookEntity> findBookBy(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection){
        Pageable pageDetails = createPageable(pageNumber, pageSize, sortBy, sortDirection);
        return bookRepository.getNewReleasedBooks(pageDetails, keyword == null ? "" : keyword);
    }

    public BookDTO mapToBookDto(BookEntity bookEntity) {
        BookDTO bookDTO = modelMapper.map(bookEntity, BookDTO.class);
        bookDTO.setPublishedDate(bookEntity.getPublishedDate() == null ? null : bookEntity.getPublishedDate().toString());
        bookDTO.setStock(getTotalStock(bookEntity));
        int discountPercent = bookEntity.getDiscountPercent() == null ? 0 : Math.max(0, Math.min(100, bookEntity.getDiscountPercent()));
        bookDTO.setDiscountPercent(discountPercent);
        bookDTO.setDiscountPrice(bookEntity.getPrice() * (100 - discountPercent) / 100);
        if (bookEntity.getAuthorEntity() != null) {
            bookDTO.setAuthorName(bookEntity.getAuthorEntity().getName());
        }
        if (bookEntity.getCategoryEntity() != null) {
            bookDTO.setCategoryName(bookEntity.getCategoryEntity().getName());
        }
        return bookDTO;
    }

    public ServerResponseDto getBookByTitle(String title) {
        List<BookEntity> bookEntityList = bookRepository.getBookByTitle(title);

        List<BookDTO> bookDTOList = bookEntityList.stream().map(this::mapToBookDto).toList();
        return ServerResponseDto.success(bookDTOList) ;
    }

    public ServerResponseDto getBookByISBN(String isbn) {
        BookEntity bookEntity = bookRepository.findBookByIsbn(isbn);
        if(bookEntity == null) {
            throw new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND);
        }

        return ServerResponseDto.success(mapToBookDto(bookEntity));
    }

    public ServerResponseDto searchBookByContent(String content) {
        String cleanText = normalizeText(content);
        if (cleanText.isBlank()) {
            throw new NoSuchElementException("Không tìm thấy sách");
        }
        List<BookEntity> allBookEntities = bookRepository.findAll();

        BookEntity bestMatch = null;
        double highestScore = 0.0;

        for (BookEntity bookEntity : allBookEntities) {
            String combined = normalizeText(String.join(" ",
                    nullToEmpty(bookEntity.getTitle()),
                    bookEntity.getAuthorEntity() == null ? "" : nullToEmpty(bookEntity.getAuthorEntity().getName()),
                    nullToEmpty(bookEntity.getDescription())
            ));
            double score = similarity(cleanText, combined);
            if (score > highestScore) {
                highestScore = score;
                bestMatch = bookEntity;
            }
        }

        if (bestMatch == null) {
            throw new NoSuchElementException("Không tìm thấy sách ");
        }

        if (highestScore >= SEARCH_MATCH_THRESHOLD) {
            return ServerResponseDto.success(mapToBookDto(bestMatch));
        }

        throw new NoSuchElementException("Không tìm thấy sách");
    }

    private String normalizeText(String input) {
        if (input == null) {
            return "";
        }
        return input.toLowerCase()
                .replaceAll("[^\\p{L}\\p{N}\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        String resolvedSortBy = isBlank(sortBy) ? DEFAULT_SORT_BY : sortBy;
        String resolvedSortDirection = isBlank(sortDirection) ? DEFAULT_SORT_DIRECTION : sortDirection;
        Sort.Direction direction = Sort.Direction.fromString(resolvedSortDirection);
        return PageRequest.of(page, size, Sort.by(direction, resolvedSortBy));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private int clampDiscountPercent(Integer value) {
        if (value == null) {
            return 0;
        }
        return Math.max(0, Math.min(100, value));
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private Long getTotalStock(BookEntity bookEntity) {
        if (bookEntity.getStoreBooks() == null || bookEntity.getStoreBooks().isEmpty()) {
            return bookEntity.getStock();
        }
        return bookEntity.getStoreBooks().stream()
                .mapToLong(storeBook -> storeBook.getStock() == null ? 0L : storeBook.getStock())
                .sum();
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
