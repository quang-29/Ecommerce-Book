package org.example.bookstore.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.AuthorEntity;
import org.example.bookstore.model.BookEntity;
import org.example.bookstore.model.BookImageEntity;
import org.example.bookstore.model.CategoryEntity;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.BookImageDTO;
import org.example.bookstore.payload.request.BookSavedRequest;
import org.example.bookstore.repository.AuthorRepository;
import org.example.bookstore.repository.BookImageRepository;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.CategoryRepository;
import org.example.bookstore.repository.StoreBookRepository;
import org.example.bookstore.utils.FileUploadUtil;
import org.example.bookstore.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.lang.Long;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Slf4j
@Service
public class BookService {

    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final StoreBookRepository storeBookRepository;
    private final BookImageRepository bookImageRepository;

    private static final String DEFAULT_SORT_BY = "id";
    private static final String DEFAULT_SORT_DIRECTION = "ASC";
    private static final double SEARCH_MATCH_THRESHOLD = 0.5;
    private static final String BOOK_IMAGE_SUB_BUCKET = "book";

    public BookService(CategoryRepository categoryRepository, FileService fileService, ModelMapper modelMapper, BookRepository bookRepository, AuthorRepository authorRepository, StoreBookRepository storeBookRepository, BookImageRepository bookImageRepository) {
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.storeBookRepository = storeBookRepository;
        this.bookImageRepository = bookImageRepository;
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
        Page<BookDTO> pageBooks = bookRepository.getPageBook(keywordSearch, pageable).map(this::mapToBookDto);
        return ServerResponseDto.success(pageBooks);
    }

    @Transactional
    public ServerResponseDto saveBook(BookSavedRequest request) throws FileUploadException {
        String bookId = request.getId();
        if (StringUtils.isNullOrEmpty(bookId)) {
            return createBook(request);
        } else {
            return updateBook(request);
        }
    }

    @Transactional
    public ServerResponseDto createBook(BookSavedRequest request) throws FileUploadException {

        CategoryEntity categoryEntity = categoryRepository.findByName(request.getCategory())
                .orElseGet(() -> {
                    CategoryEntity newCategoryEntity = new CategoryEntity();
                    newCategoryEntity.setName(request.getCategory());
                    return categoryRepository.save(newCategoryEntity);
                });

        BookEntity bookEntity = BookEntity.builder()
                .title(request.getTitle())
                .volumeNumber(request.getVolumeNumber())
                .price(request.getPrice())
                .discountPercent(clampDiscountPercent(request.getDiscountPercent()))
                .description(request.getDescription())
                .language(request.getLanguage())
                .isbn(request.getIsbn())
                .page(request.getPage())
                .publisher(request.getPublisher())
                .reprint(request.getReprint())
                .stock(request.getStock())
                .publishedDate(request.getPublishedDate())
                .sold(0L)
                .build();
        bookEntity.setCategoryId(categoryEntity.getId());

        Optional<AuthorEntity> optionalAuthor = authorRepository.findByName(request.getAuthor());
        if(optionalAuthor.isPresent()){
            bookEntity.setAuthorId(optionalAuthor.get().getId());
        } else {
            AuthorEntity authorEntity = new AuthorEntity();
            authorEntity.setName(request.getAuthor());
            authorRepository.save(authorEntity);
            bookEntity.setAuthorId(authorEntity.getId());
        }
        bookRepository.save(bookEntity);

        List<MultipartFile> images = request.getImages();
        if (images != null && !images.isEmpty()) {
            for (int sortOrder = 0; sortOrder < images.size(); sortOrder++) {
                BookImageDTO bookImageDTO = uploadAndSaveBookImage(bookEntity.getId(), images.get(sortOrder), sortOrder);
                if (sortOrder == 0) {
                    bookEntity.setImagePath(bookImageDTO.getUrl());
                }
            }
            bookRepository.save(bookEntity);
        }
        return ServerResponseDto.success(mapToBookDto(bookEntity));
    }

    @Transactional
    public ServerResponseDto updateBook(BookSavedRequest request) {
        Optional<BookEntity> foundBookEntity = bookRepository.findById(Long.valueOf(request.getId()));
        if(foundBookEntity.isEmpty()) {
            log.info("Book with id: {} is already existed in data", request.getId());
            throw new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND);
        }
        BookEntity book = foundBookEntity.get();
        book.setDiscountPercent(clampDiscountPercent(request.getDiscountPercent()));
        book.setPublishedDate(request.getPublishedDate());
        BookEntity bookEntitySaved = bookRepository.save(book);
        return ServerResponseDto.success(mapToBookDto(bookEntitySaved));
    }

    @Transactional
    public ServerResponseDto uploadImageBook(Long id, MultipartFile file) throws FileUploadException {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        BookImageDTO bookImageDTO = uploadAndSaveBookImage(id, file, nextSortOrderFor(id));
        bookEntity.setImagePath(bookImageDTO.getUrl());
        bookRepository.save(bookEntity);
        return ServerResponseDto.success(bookImageDTO);
    }

    @Transactional
    public ServerResponseDto uploadImagesBook(Long bookId, List<MultipartFile> files) throws FileUploadException {
        bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));

        int nextSortOrder = nextSortOrderFor(bookId);
        List<BookImageDTO> uploaded = new ArrayList<>();
        for (MultipartFile file : files) {
            uploaded.add(uploadAndSaveBookImage(bookId, file, nextSortOrder++));
        }
        return ServerResponseDto.success(uploaded);
    }

    private int nextSortOrderFor(Long bookId) {
        return bookImageRepository.findByBookIdOrderBySortOrderAsc(bookId).stream()
                .mapToInt(BookImageEntity::getSortOrder)
                .max()
                .orElse(-1) + 1;
    }

    private BookImageDTO uploadAndSaveBookImage(Long bookId, MultipartFile file, int sortOrder) throws FileUploadException {
        FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
        String url = fileService.uploadFile(BOOK_IMAGE_SUB_BUCKET, file)
                .orElseThrow(() -> new FileUploadException(MessageException.FILE_UPLOAD_ERROR.getMessage()));

        BookImageEntity bookImageEntity = BookImageEntity.builder()
                .bookId(bookId)
                .imageUrl(url)
                .sortOrder(sortOrder)
                .build();
        bookImageRepository.save(bookImageEntity);
        return new BookImageDTO(bookImageEntity.getId(), bookImageEntity.getImageUrl(), bookImageEntity.getSortOrder());
    }

    @Transactional
    public ServerResponseDto deleteBookImage(Long bookId, Long imageId) {
        BookImageEntity bookImageEntity = bookImageRepository.findById(imageId)
                .filter(image -> image.getBookId().equals(bookId))
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_IMAGE_NOT_FOUND));
        bookImageRepository.delete(bookImageEntity);
        return ServerResponseDto.success("Delete book image successfully");
    }

    public ServerResponseDto updateBook(Long id, BookDTO bookDTO) {
        BookEntity bookEntityFound = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        modelMapper.map(bookDTO, bookEntityFound);
        bookEntityFound.setDiscountPercent(clampDiscountPercent(bookDTO.getDiscountPercent()));
        AuthorEntity authorEntity = authorRepository.findByName(bookDTO.getAuthorName())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.AUTHOR_NOT_FOUND));
        bookEntityFound.setAuthorId(authorEntity.getId());
        CategoryEntity categoryEntity = categoryRepository.findByName(bookDTO.getCategoryName())
                .orElseGet(() -> {
                    CategoryEntity newCategoryEntity = new CategoryEntity();
                    newCategoryEntity.setName(bookDTO.getCategoryName());
                    return categoryRepository.save(newCategoryEntity);
                });
        bookEntityFound.setCategoryId(categoryEntity.getId());
        BookEntity savedBookEntity = bookRepository.save(bookEntityFound);
        return ServerResponseDto.success(mapToBookDto(savedBookEntity));
    }

    public ServerResponseDto deleteBook(Long id) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        bookRepository.delete(bookEntity);
        return ServerResponseDto.success("Delete book Successfully");
    }

    public ServerResponseDto getBookUpSale(int pageNumber, int pageSize, String sortBy, String sortDirection, String keywordSearch) {
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, sortDirection);
        Page<BookDTO> pageBooks = bookRepository.getBookUpSale(keywordSearch, pageable).map(this::mapToBookDto);
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
        if (bookEntity.getAuthorId() != null) {
            authorRepository.findById(bookEntity.getAuthorId()).ifPresent(author -> bookDTO.setAuthorName(author.getName()));
        }
        if (bookEntity.getCategoryId() != null) {
            categoryRepository.findById(bookEntity.getCategoryId()).ifPresent(category -> bookDTO.setCategoryName(category.getName()));
        }
        List<BookImageDTO> images = bookImageRepository.findByBookIdOrderBySortOrderAsc(bookEntity.getId()).stream()
                .map(image -> new BookImageDTO(image.getId(), image.getImageUrl(), image.getSortOrder()))
                .toList();
        bookDTO.setImages(images);
        return bookDTO;
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
            String authorName = bookEntity.getAuthorId() == null ? ""
                    : authorRepository.findById(bookEntity.getAuthorId()).map(AuthorEntity::getName).orElse("");
            String combined = normalizeText(String.join(" ",
                    nullToEmpty(bookEntity.getTitle()),
                    nullToEmpty(authorName),
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
        List<org.example.bookstore.model.StoreBookEntity> storeBooks = storeBookRepository.findByBookId(bookEntity.getId());
        if (storeBooks.isEmpty()) {
            return bookEntity.getStock();
        }
        return storeBooks.stream()
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
