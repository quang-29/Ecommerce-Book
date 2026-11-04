package org.example.bookstore.service;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.enums.Roles;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.BookEntity;
import org.example.bookstore.model.StoreBookEntity;
import org.example.bookstore.model.StoreEntity;
import org.example.bookstore.model.UserEntity;
import org.example.bookstore.payload.shop.StoreBookCreateRequest;
import org.example.bookstore.payload.shop.StoreBookDTO;
import org.example.bookstore.payload.shop.StoreBookUpdateRequest;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.StoreBookRepository;
import org.example.bookstore.repository.StoreRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.security.CurrentUserDetails;
import org.example.bookstore.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShopService {

    private static final Set<String> SORTABLE_FIELDS = Set.of("id", "stock", "price", "active");

    private final StoreRepository storeRepository;
    private final StoreBookRepository storeBookRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ShopService(StoreRepository storeRepository, StoreBookRepository storeBookRepository,
                       BookRepository bookRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.storeBookRepository = storeBookRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public ServerResponseDto getMyStores() {
        CustomUserDetails current = CurrentUserDetails.getCurrentUser();
        if (current == null) {
            throw new ResourceNotFoundException(MessageException.UNAUTHENTICATED);
        }
        return ServerResponseDto.success(storeRepository.findByOwnerUserId(current.getUserId()));
    }

    // Admin gán chủ cho cửa hàng. User cần đăng nhập lại để JWT mang role mới.
    @Transactional
    public ServerResponseDto assignOwner(Long storeId, Long userId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.STORE_NOT_FOUND));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));
        if (user.getRoles() == Roles.USER) {
            user.setRoles(Roles.SHOP_OWNER);
            userRepository.save(user);
        }
        store.setOwnerUserId(userId);
        storeRepository.save(store);
        return ServerResponseDto.success("Assigned owner successfully");
    }

    public ServerResponseDto getStoreBooks(Long storeId, String keyword, int page, int size,
                                           String sortBy, String sortDirection) {
        String field = sortBy != null && SORTABLE_FIELDS.contains(sortBy) ? sortBy : "id";
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), Sort.by(direction, field));
        String kw = keyword == null || keyword.isBlank() ? null : keyword.trim();

        Page<StoreBookEntity> result = storeBookRepository.searchByStore(storeId, kw, pageable);
        Map<Long, BookEntity> booksById = loadBooks(result.getContent());
        List<StoreBookDTO> dtos = result.getContent().stream()
                .map(sb -> toDto(sb, booksById.get(sb.getBookId())))
                .collect(Collectors.toList());
        return ServerResponseDto.success(new PageImpl<>(dtos, pageable, result.getTotalElements()));
    }

    @Transactional
    public ServerResponseDto addBookToStore(Long storeId, StoreBookCreateRequest request) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.STORE_NOT_FOUND));
        BookEntity book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        if (storeBookRepository.findByStoreIdAndBookId(storeId, book.getId()).isPresent()) {
            throw new ResourceNotFoundException(MessageException.BOOK_EXISTED);
        }
        StoreBookEntity storeBook = new StoreBookEntity();
        storeBook.setStoreId(storeId);
        storeBook.setBookId(book.getId());
        storeBook.setPrice(request.getPrice());
        storeBook.setStock(request.getStock());
        storeBook.setActive(true);
        return ServerResponseDto.success(toDto(storeBookRepository.save(storeBook), book));
    }

    @Transactional
    public ServerResponseDto updateStoreBook(Long storeId, Long storeBookId, StoreBookUpdateRequest request) {
        // Khóa dòng để không ghi đè lên việc trừ tồn kho của đơn hàng đang đặt cùng lúc
        StoreBookEntity storeBook = findOwnedStoreBookForUpdate(storeId, storeBookId);
        if (request.getPrice() != null) {
            storeBook.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            storeBook.setStock(request.getStock());
        }
        if (request.getActive() != null) {
            storeBook.setActive(request.getActive());
        }
        StoreBookEntity saved = storeBookRepository.save(storeBook);
        return ServerResponseDto.success(toDto(saved, bookRepository.findById(saved.getBookId()).orElse(null)));
    }

    // Ngừng bán (active=false) thay vì xóa, vì cart_items/order_items tham chiếu store_book
    @Transactional
    public ServerResponseDto deactivateStoreBook(Long storeId, Long storeBookId) {
        StoreBookEntity storeBook = findOwnedStoreBookForUpdate(storeId, storeBookId);
        storeBook.setActive(false);
        storeBookRepository.save(storeBook);
        return ServerResponseDto.success("Store book deactivated");
    }

    private StoreBookEntity findOwnedStoreBookForUpdate(Long storeId, Long storeBookId) {
        StoreBookEntity storeBook = storeBookRepository.findByIdForUpdate(storeBookId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        if (!storeId.equals(storeBook.getStoreId())) {
            throw new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND);
        }
        return storeBook;
    }

    private Map<Long, BookEntity> loadBooks(List<StoreBookEntity> storeBooks) {
        List<Long> ids = storeBooks.stream().map(StoreBookEntity::getBookId).distinct().collect(Collectors.toList());
        return bookRepository.findAllById(ids).stream().collect(Collectors.toMap(BookEntity::getId, Function.identity()));
    }

    private StoreBookDTO toDto(StoreBookEntity sb, BookEntity book) {
        StoreBookDTO dto = new StoreBookDTO();
        dto.setStoreBookId(sb.getId());
        dto.setStoreId(sb.getStoreId());
        dto.setBookId(sb.getBookId());
        dto.setStock(sb.getStock());
        dto.setActive(sb.isActive());
        if (book != null) {
            dto.setTitle(book.getTitle());
            dto.setIsbn(book.getIsbn());
            dto.setImagePath(book.getImagePath());
            dto.setBasePrice(sb.getBasePrice(book));
            dto.setDiscountPercent(sb.getDiscountPercent(book));
            dto.setEffectivePrice(sb.getEffectivePrice(book));
        }
        return dto;
    }
}
