package org.example.bookstore.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.enums.ErrorCode;
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
import org.example.bookstore.service.Interface.BookService;
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
public class BookServiceImpl implements BookService {
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

    @Override
    public BookDTO getBookById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
        return bookDTO;
    }

    @Override
    public BookResponse getAllBooks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Book> pageBooks = bookRepository.findAll(pageDetails);
        List<Book> books = pageBooks.getContent();

        List<BookDTO> bookDTOList = books.stream()
                .map(book -> {
                    BookDTO bookDTO = modelMapper.map(book, BookDTO.class);

                    // Kiểm tra null trước khi gọi getName()
                    if (book.getAuthor() != null) {
                        bookDTO.setAuthorName(book.getAuthor().getName());
                    } else {
                        bookDTO.setAuthorName("Không xác định");
                    }

                    if (book.getCategory() != null) {
                        bookDTO.setCategoryName(book.getCategory().getName());
                    } else {
                        bookDTO.setCategoryName("Chưa phân loại");
                    }

                    return bookDTO;
                })
                .toList();

        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(bookDTOList);
        bookResponse.setPageNumber(pageBooks.getNumber());
        bookResponse.setPageSize(pageBooks.getSize());
        bookResponse.setTotalElements(pageBooks.getTotalElements());
        bookResponse.setTotalPages(pageBooks.getTotalPages());
        bookResponse.setLastPage(pageBooks.isLast());

        return bookResponse;
    }


    @Override
    public BookResponse getAllBooksByAuthor(String authorName,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Book> pageBooks = bookRepository.findByAuthor_Name(authorName, pageDetails);
        List<BookDTO> bookDTOS =  pageBooks.getContent().stream()
                .map(book -> {
                            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                            bookDTO.setAuthorName(book.getAuthor().getName());
                            bookDTO.setCategoryName(book.getCategory().getName());
                            return bookDTO;
                        }
                ).toList();
        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(bookDTOS);
        bookResponse.setPageNumber(pageBooks.getNumber());
        bookResponse.setPageSize(pageBooks.getSize());
        bookResponse.setTotalElements(pageBooks.getTotalElements());
        bookResponse.setTotalPages(pageBooks.getTotalPages());
        bookResponse.setLastPage(pageBooks.isLast());
        return bookResponse;
    }

    @Override
    public BookResponse getAllBooksByCategory(String category,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Book> pageBooks = bookRepository.findByCategory_Name(category, pageDetails);
        List<BookDTO> bookDTOS =  pageBooks.getContent().stream()
                .map(book -> {
                            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                            bookDTO.setAuthorName(book.getAuthor().getName());
                            bookDTO.setCategoryName(book.getCategory().getName());
                            return bookDTO;
                        }
                ).toList();
        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(bookDTOS);
        bookResponse.setPageNumber(pageBooks.getNumber());
        bookResponse.setPageSize(pageBooks.getSize());
        bookResponse.setTotalElements(pageBooks.getTotalElements());
        bookResponse.setTotalPages(pageBooks.getTotalPages());
        bookResponse.setLastPage(pageBooks.isLast());
        return bookResponse;

    }

    @Override
    @Transactional
    public BookDTO addBook(CreateBookRequest request) {
        Book foundBook = bookRepository.findByName(request.getTitle());
        if(foundBook != null) {
            throw new AppException(ErrorCode.BOOK_EXISTED);
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
        return bookDTO;


    }
    @Override
    @Transactional
    public CloudinaryResponse uploadImageBook(UUID id, MultipartFile file) {
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
                return response;
            } catch (Exception ex){
                throw new RuntimeException(ex.getMessage());
            }
        }

    @Override
    @Transactional
    public String uploadImageB(UUID id, MultipartFile file) {
        try {
            Optional<Book> optionalBook = bookRepository.findById(id);
            if(optionalBook.isEmpty()){
                throw new AppException(ErrorCode.BOOK_NOT_FOUND);
            }
            String url = awsS3Service.saveImageToS3(file);
            Book book = optionalBook.get();
            book.setImagePath(url);
            bookRepository.save(book);
            return url;
        } catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public BookDTO updateBook(UUID id, BookDTO bookDTO) {
        Book bookFound = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        modelMapper.map(bookDTO, bookFound);
        Author author = authorRepository.findByName(bookDTO.getAuthorName())
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
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
        return updatedBookDTO;
    }

//        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
//            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//            List<BookDTO> products = cart.getCartItems().stream()
//                    .map(p -> modelMapper.map(p.getBook(), ProductDTO.class)).collect(Collectors.toList());
//
//            cartDTO.setProducts(products);
//
//            return cartDTO;
//
//        }).collect(Collectors.toList());
//
//        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));



    @Override
    public boolean deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book","bookId",id));
        bookRepository.delete(book);
        return true;
    }

    @Override
    public BookResponse getBookUpSale(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Book> pageBooks = bookRepository.getBookUpSale(pageable);
        List<BookDTO> listBookDTO = pageBooks.getContent().stream()
                .map(book -> {
                            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                            bookDTO.setAuthorName(book.getAuthor().getName());
                            bookDTO.setCategoryName(book.getCategory().getName());
                            return bookDTO;
                        }
                ).toList();
        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(listBookDTO);
        bookResponse.setPageNumber(pageBooks.getNumber());
        bookResponse.setPageSize(pageBooks.getSize());
        bookResponse.setTotalElements(pageBooks.getTotalElements());
        bookResponse.setTotalPages(pageBooks.getTotalPages());
        bookResponse.setLastPage(pageBooks.isLast());
        return bookResponse;
    }

    @Override
    public BookResponse getNewReleaseBook(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Book> pageBooks = bookRepository.getNewReleasedBooks(pageDetails);
        List<BookDTO> bookDTOS =  pageBooks.getContent().stream()
                .map(book -> {
                            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                            bookDTO.setAuthorName(book.getAuthor().getName());
                            bookDTO.setCategoryName(book.getCategory().getName());
                            return bookDTO;
                        }
                ).toList();
        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(bookDTOS);
        bookResponse.setPageNumber(pageBooks.getNumber());
        bookResponse.setPageSize(pageBooks.getSize());
        bookResponse.setTotalElements(pageBooks.getTotalElements());
        bookResponse.setTotalPages(pageBooks.getTotalPages());
        bookResponse.setLastPage(pageBooks.isLast());
        return bookResponse;
    }

    @Override
    public List<BookDTO> getBookByTitle(String title) {
        List<Book> bookList = bookRepository.getBookByTitle(title);

        List<BookDTO> bookDTOList = bookList.stream().map(book -> modelMapper.map(book, BookDTO.class)).toList();
        return bookDTOList ;
    }

    @Override
    public BookDTO getBookByISBN(String isbn) {
//        String fixISBN = isbn.replace("-", "");
        Book book = bookRepository.findBookByIsbn(isbn);
        if(book == null) {
            throw new AppException(ErrorCode.BOOK_NOT_FOUND);
        }

        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public BookDTO searchBookByContent(String content) {
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
            // Có thể throw exception hoặc trả về null tùy yêu cầu
            throw new NoSuchElementException("Không tìm thấy sách ");
        }

        if (bestMatch != null && highestScore >= 0.5) {
            return modelMapper.map(bestMatch, BookDTO.class);
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
                        Math.min(dp[i - 1][j] + 1,     // delete
                                dp[i][j - 1] + 1),    // insert
                        dp[i - 1][j - 1] + cost        // replace
                );
            }
        }
        return dp[s1.length()][s2.length()];
    }


}
