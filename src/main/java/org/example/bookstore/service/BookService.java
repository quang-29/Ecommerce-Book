package org.example.bookstore.service;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.Author;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.request.CreateBookRequest;
import org.example.bookstore.payload.response.BookResponse;
import org.example.bookstore.payload.response.CloudinaryResponse;
import org.example.bookstore.repository.AuthorRepository;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.CategoryRepository;
import org.example.bookstore.service.Interface.AwsS3Service;
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


import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private CloudinaryServiceImpl cloudinaryServiceImpl;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AwsS3Service awsS3Service;


    public ServerResponseDto getBookById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageException.AUTHOR_NOT_FOUND.getMessage()));
        return ServerResponseDto.success( modelMapper.map(book, BookDTO.class));
    }


    public ServerResponseDto getAllBooks(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<BookDTO> pageBooks = bookRepository.findAll(pageable).map(book -> modelMapper.map(book, BookDTO.class));
        return ServerResponseDto.success(pageBooks);
    }

    public ServerResponseDto getAllBooksByAuthor(String authorName,int page, int size, String sortBy, String sortDirection) {

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
        Book foundBook = bookRepository.findByName(request.getTitle());
        if(foundBook != null) {
            throw new RuntimeException(MessageException.BOOK_EXIST.getMessage());
        }
        Book book = modelMapper.map(request, Book.class);
        book.setSold(0L);
        book.setPublishedDate(LocalDate.parse(request.getPublishedDate()));
        Optional<Category> optionalCategory = categoryRepository.findByName(request.getCategory());
        if(optionalCategory.isPresent()){
            book.setCategory(optionalCategory.get());
        }
        Optional<Author> optionalAuthor = authorRepository.findByName(request.getAuthor());
        if(optionalAuthor.isPresent()){
            book.setAuthor(optionalAuthor.get());
        } else {
            Author author = new Author();
            author.setName(request.getAuthor());
            authorRepository.save(author);
            book.setAuthor(author);
        }
        Book bookSaved = bookRepository.save(book);
        BookDTO bookDTO = modelMapper.map(bookSaved, BookDTO.class);
        bookDTO.setAuthorName(bookSaved.getAuthor().getName());
        return ServerResponseDto.success(bookDTO);


    }

    @Transactional
    public ServerResponseDto uploadImageBook(UUID id, MultipartFile file) {
            try {
                Optional<Book> optionalBook = bookRepository.findById(id);
                if(optionalBook.isEmpty()){
                    throw new AppException(ErrorCode.BOOK_NOT_FOUND);
                }
                Book book = optionalBook.get();
                FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
                final String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
                final CloudinaryResponse response = cloudinaryServiceImpl.uploadFile(file, fileName);
                book.setImagePath(response.getUrl());
                bookRepository.save(book);
                return ServerResponseDto.success(response);
            } catch (Exception ex){
                throw new RuntimeException(ex.getMessage());
            }
        }


    public ServerResponseDto updateBook(UUID id, BookDTO bookDTO) {
        Book bookFound = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageException.BOOK_NOT_FOUND.getMessage()));
        modelMapper.map(bookDTO, bookFound);
        Author author = authorRepository.findByName(bookDTO.getAuthorName())
                .orElseThrow(() -> new RuntimeException(MessageException.AUTHOR_NOT_FOUND.getMessage()));
        bookFound.setAuthor(author);
        Category category = categoryRepository.findByName(bookDTO.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(bookDTO.getCategoryName());
                    return categoryRepository.save(newCategory);
                });
        bookFound.setCategory(category);
        Book savedBook = bookRepository.save(bookFound);
        BookDTO updatedBookDTO = modelMapper.map(savedBook, BookDTO.class);
        updatedBookDTO.setAuthorName(savedBook.getAuthor().getName());
        updatedBookDTO.setCategoryName(savedBook.getCategory().getName());
        return ServerResponseDto.success(updatedBookDTO);
    }

    public ServerResponseDto deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book","bookId", id));
        bookRepository.delete(book);
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
        List<Book> bookList = bookRepository.getBookByTitle(title);

        List<BookDTO> bookDTOList = bookList.stream().map(book -> modelMapper.map(book, BookDTO.class)).toList();
        return ServerResponseDto.success(bookDTOList) ;
    }

    public ServerResponseDto getBookByISBN(String isbn) {
        Book book = bookRepository.findBookByIsbn(isbn);
        if(book == null) {
            throw new RuntimeException(MessageException.BOOK_NOT_FOUND.getMessage());
        }

        return ServerResponseDto.success(modelMapper.map(book, BookDTO.class));
    }


    public ServerResponseDto searchBookByContent(String content) {
        String cleanText = normalizeText(content);
        List<Book> allBooks = bookRepository.findAll();

        Book bestMatch = null;
        double highestScore = 0.0;

        for (Book book : allBooks) {
            String combined = (book.getTitle() + " " + book.getAuthor() + " " + book.getDescription()).toLowerCase();
            double score = similarity(cleanText, combined);
            if (score > highestScore) {
                highestScore = score;
                bestMatch = book;
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
