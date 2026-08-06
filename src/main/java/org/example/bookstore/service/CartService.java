package org.example.bookstore.service;


import jakarta.persistence.LockTimeoutException;
import jakarta.transaction.Transactional;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.BookEntity;
import org.example.bookstore.model.CartEntity;
import org.example.bookstore.model.CartItemEntity;
import org.example.bookstore.model.StoreBookEntity;
import org.example.bookstore.model.StoreEntity;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.CartDTO;
import org.example.bookstore.payload.CartItemDTO;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.CartItemRepository;
import org.example.bookstore.repository.CartRepository;
import org.example.bookstore.repository.StoreBookRepository;
import org.example.bookstore.repository.StoreRepository;
import org.example.bookstore.service.Interface.BaseRedisService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.lang.Long;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final StoreBookRepository storeBookRepository;
    private final StoreRepository storeRepository;
    private final BaseRedisService<String, String, Object> redisService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private static final String CART_CACHE_KEY_PREFIX = "cart:";
    private static final long CART_CACHE_TTL_DAYS = 30;

    public CartService(CartRepository cartRepository, BookRepository bookRepository, CartItemRepository cartItemRepository, StoreBookRepository storeBookRepository, StoreRepository storeRepository, BaseRedisService<String, String, Object> redisService, ApplicationEventPublisher eventPublisher) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.cartItemRepository = cartItemRepository;
        this.storeBookRepository = storeBookRepository;
        this.storeRepository = storeRepository;
        this.redisService = redisService;
        this.eventPublisher = eventPublisher;
    }

    // Kept for API compatibility; currently unused (see CartController — it always calls the
    // 5-arg overload directly). Marked @Transactional too so that IF something ever calls this
    // overload, the pessimistic lock taken inside the 5-arg overload is actually held for the
    // whole operation instead of being silently dropped by self-invocation (see note on the
    // 5-arg overload below for why that matters).
    @Transactional
    public CartDTO addProductToCart(Long cartId, Long bookId, Integer quantity) {
        return addProductToCart(cartId, null, bookId, null, quantity);
    }

    @Transactional
    public CartDTO addProductToCart(Long cartId, Long bookId, Long storeId, Integer quantity) {
        return addProductToCart(cartId, null, bookId, storeId, quantity);
    }

    @Transactional
    public CartDTO addProductToCart(Long cartId, Long storeBookId, Long bookId, Long storeId, Integer quantity) {
        return withLockErrorHandling(() -> {
            // Lock order: Cart first, StoreBook second (see repository comments) — every
            // method in this class that needs both locks must acquire them in this order.
            CartEntity cartEntity = cartRepository.findByIdForUpdate(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));
            StoreBookEntity storeBookEntity = resolveStoreBookForUpdate(storeBookId, bookId, storeId);
            BookEntity bookEntity = bookRepository.findById(storeBookEntity.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));

            if (storeBookEntity.getStock() < quantity) {
                throw new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM);
            }

            CartItemEntity cartItemEntity = cartItemRepository.findByCartIdAndStoreBookId(cartId, storeBookEntity.getId());

            if (cartItemEntity != null) {
                cartItemEntity.setQuantity(cartItemEntity.getQuantity() + quantity);
                cartItemEntity.setBookPrice(storeBookEntity.getEffectivePrice(bookEntity));
                cartItemRepository.save(cartItemEntity);
            } else {
                CartItemEntity newCartItemEntity = new CartItemEntity();
                newCartItemEntity.setCartId(cartEntity.getId());
                newCartItemEntity.setBookId(bookEntity.getId());
                newCartItemEntity.setStoreBookId(storeBookEntity.getId());
                newCartItemEntity.setQuantity(quantity);
                newCartItemEntity.setBookPrice(storeBookEntity.getEffectivePrice(bookEntity));
                cartItemRepository.save(newCartItemEntity);
            }

            updateCartTotalPrice(cartEntity);
            cartRepository.save(cartEntity);
            // Deferred to AFTER_COMMIT (see onCartChanged) instead of calling syncCartToRedis
            // directly here — otherwise a rollback after this line would leave Redis holding
            // data that was never actually committed to the database.
            eventPublisher.publishEvent(new CartChangedEvent(cartId));

            return buildCartDtoFromDatabase(cartId);
        });
    }

    private void updateCartTotalPrice(CartEntity cartEntity) {
        long totalPrice = cartItemRepository.findByCartId(cartEntity.getId()).stream()
                .mapToLong(cartItem -> cartItem.getBookPrice() * cartItem.getQuantity())
                .sum();
        cartEntity.setTotalPrice(totalPrice);
    }

    public List<CartDTO> getAllCarts() {
        List<CartEntity> cartEntities = cartRepository.findAll();
        if (cartEntities.isEmpty()) {
            throw new ResourceNotFoundException(MessageException.CART_NOT_FOUND);
        }
        List<CartDTO> cartDTOs = cartEntities.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<CartItemDTO> cartItemDTOS = cartItemRepository.findByCartId(cart.getId()).stream()
                    .map(p -> modelMapper.map(bookRepository.findById(p.getBookId()).orElse(null), CartItemDTO.class))
                    .collect(Collectors.toList());
            cartDTO.setCartItem(cartItemDTOS);
            return cartDTO;
        }).collect(Collectors.toList());
        return cartDTOs;
    }

    public CartDTO getCartByUserId(String userId) {
        CartEntity cartEntity = cartRepository.getCartByUserId(userId);
        if (cartEntity == null) {
            throw new ResourceNotFoundException(MessageException.CART_NOT_FOUND);
        }

        CartDTO cachedCart = getCartFromRedis(cartEntity.getId());
        if (cachedCart != null) {
            return cachedCart;
        }

        // Read-only path, no DB mutation/transaction here, so there's no rollback risk —
        // safe to sync Redis synchronously instead of going through the AFTER_COMMIT event.
        CartDTO cartDTO = buildCartDtoFromDatabase(cartEntity.getId());
        syncCartToRedis(cartEntity.getId());
        return cartDTO;
    }

    // Kept for API compatibility; currently unused externally (only UserService.deleteUser
    // calls the 2-arg overload below). Each overload already carries its own @Transactional,
    // so whichever one is entered from outside this class correctly opens the transaction
    // that the self-invocation into the 4-arg overload then joins (propagation REQUIRED) —
    // unlike the addProductToCart overloads, this one was already safe before this review.
    @Transactional
    public boolean deleteProductFromCart(Long cartId, Long bookId) {
        return deleteProductFromCart(cartId, null, bookId, null);
    }

    @Transactional
    public boolean deleteProductFromCart(Long cartId, Long bookId, Long storeId) {
        return deleteProductFromCart(cartId, null, bookId, storeId);
    }

    @Transactional
    public boolean deleteProductFromCart(Long cartId, Long storeBookId, Long bookId, Long storeId) {
        return withLockErrorHandling(() -> {
            CartEntity cartEntity = cartRepository.findByIdForUpdate(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));

            CartItemEntity cartItemEntity = findCartItem(cartId, storeBookId, bookId, storeId);
            if (cartItemEntity == null) {
                throw new ResourceNotFoundException(MessageException.CART_NO_FOUND_BOOK);
            }

            deleteCartItemRow(cartId, cartItemEntity, bookId);
            // Recompute the total from the remaining rows instead of subtracting the removed
            // item's price*quantity from the old total — a pure re-sum can never go negative,
            // so it fixes the bug at the source instead of masking it with Math.max(0, ...).
            updateCartTotalPrice(cartEntity);
            cartRepository.save(cartEntity);
            eventPublisher.publishEvent(new CartChangedEvent(cartId));
            return true;
        });
    }

    // Kept for API compatibility; currently unused externally. Already carried its own
    // @Transactional before this review, so — like the delete overloads above — the
    // self-invocation into the 4-arg overload was already safe.
    @Transactional
    public CartDTO decreaseProductFromCart(Long cartId, Long bookId) {
        return decreaseProductFromCart(cartId, null, bookId, null);
    }

    @Transactional
    public CartDTO decreaseProductFromCart(Long cartId, Long bookId, Long storeId) {
        return decreaseProductFromCart(cartId, null, bookId, storeId);
    }

    @Transactional
    public CartDTO decreaseProductFromCart(Long cartId, Long storeBookId, Long bookId, Long storeId) {
        return withLockErrorHandling(() -> {
            CartEntity cartEntity = cartRepository.findByIdForUpdate(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));

            CartItemEntity cartItemEntity = findCartItem(cartId, storeBookId, bookId, storeId);
            if (cartItemEntity == null) {
                throw new ResourceNotFoundException(MessageException.CART_NO_FOUND_BOOK);
            }

            if (cartItemEntity.getQuantity() <= 1) {
                // Previously called deleteProductFromCart(...) directly here — a self-invocation
                // that (a) bypasses the @Transactional proxy for that call and (b) re-loads the
                // cart, re-saves it, and re-syncs Redis a second time for one logical operation.
                // Calling the shared row-delete helper directly avoids both problems: no second
                // lock acquisition, no second save, no second cache sync.
                deleteCartItemRow(cartId, cartItemEntity, bookId);
            } else {
                cartItemEntity.setQuantity(cartItemEntity.getQuantity() - 1);
                cartItemRepository.save(cartItemEntity);
            }

            updateCartTotalPrice(cartEntity);
            cartRepository.save(cartEntity);
            eventPublisher.publishEvent(new CartChangedEvent(cartId));
            return buildCartDtoFromDatabase(cartId);
        });
    }

    @Transactional
    public void clearCart(Long cartId) {
        withLockErrorHandling(() -> {
            CartEntity cartEntity = cartRepository.findByIdForUpdate(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));
            cartItemRepository.deleteByCartId(cartId);
            cartEntity.setTotalPrice(0);
            cartRepository.save(cartEntity);
            // Same AFTER_COMMIT reasoning as the other mutating methods — deleting the
            // Redis entry directly here would wipe the cache even if this transaction
            // later rolled back.
            eventPublisher.publishEvent(new CartChangedEvent(cartId));
        });
    }

    // Deletes exactly one CartItemEntity row for an already-resolved cart item. Deliberately
    // does NOT touch CartEntity/totalPrice/Redis — callers (deleteProductFromCart and
    // decreaseProductFromCart) own those steps so each logical operation performs them once.
    private void deleteCartItemRow(Long cartId, CartItemEntity cartItemEntity, Long bookId) {
        Long storeBookId = cartItemEntity.getStoreBookId();
        if (storeBookId != null) {
            cartItemRepository.deleteByCartIdAndStoreBookId(cartId, storeBookId);
        } else {
            // bookId (the method parameter) can be null when the caller only supplied
            // storeBookId — fall back to the resolved cart item's own book id instead of
            // passing null into the delete query (which would either NPE on unboxing in
            // JPQL parameter binding or silently match nothing).
            Long resolvedBookId = bookId != null ? bookId : cartItemEntity.getBookId();
            cartItemRepository.deleteCartItemByCartIdAndBookId(cartId, resolvedBookId);
        }
    }

    // Runs after the enclosing transaction has actually committed — this is the only place
    // that touches Redis for the write paths above, so a rollback never leaves stale/ahead
    // data in the cache. fallbackExecution=true covers the defensive case where this event
    // is ever published outside an active transaction (shouldn't happen given every publisher
    // above is @Transactional, but then it just runs the sync immediately instead of silently
    // never firing).
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onCartChanged(CartChangedEvent event) {
        syncCartToRedis(event.cartId());
    }

    private CartDTO buildCartDtoFromDatabase(Long cartId) {
        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cartId);

        List<CartItemEntity> cartItems = cartItemRepository.findByCartId(cartId);
        if(cartItems.isEmpty()){
            logger.info("Empty cart!");
            cartDTO.setTotalPrice(0);
            return cartDTO;
        }

        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        long totalPrice = 0;
        for (CartItemEntity cartItem : cartItems) {
            CartItemDTO cartItemDTO = mapToCartItemDto(cartItem);
            totalPrice += cartItemDTO.getBookPrice() * cartItemDTO.getQuantity();
            cartItemDTOS.add(cartItemDTO);
        }
        cartDTO.setCartItem(cartItemDTOS);
        cartDTO.setTotalPrice(totalPrice);
        return cartDTO;
    }

    private CartDTO getCartFromRedis(Long cartId) {
        try {
            Map<String, Object> cachedItems = redisService.getField(cartCacheKey(cartId));
            if (cachedItems == null || cachedItems.isEmpty()) {
                return null;
            }

            CartDTO cartDTO = new CartDTO();
            cartDTO.setCartId(cartId);
            List<CartItemDTO> cartItemDTOS = new ArrayList<>();
            long totalPrice = 0;

            for (Map.Entry<String, Object> entry : cachedItems.entrySet()) {
                Long storeBookId = Long.valueOf(entry.getKey());
                Integer quantity = toInteger(entry.getValue());
                StoreBookEntity storeBookEntity = storeBookRepository.findById(storeBookId)
                        .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
                BookEntity bookEntity = bookRepository.findById(storeBookEntity.getBookId())
                        .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
                StoreEntity storeEntity = storeRepository.findById(storeBookEntity.getStoreId())
                        .orElseThrow(() -> new ResourceNotFoundException(MessageException.STORE_NOT_FOUND));

                CartItemDTO cartItemDTO = new CartItemDTO();
                CartItemEntity cartItemEntity = cartItemRepository.findByCartIdAndStoreBookId(cartId, storeBookId);
                cartItemDTO.setCartItemId(cartItemEntity == null ? null : cartItemEntity.getId());
                cartItemDTO.setStoreId(storeEntity.getId());
                cartItemDTO.setStoreName(storeEntity.getName());
                cartItemDTO.setQuantity(quantity);
                cartItemDTO.setBook(modelMapper.map(bookEntity, BookDTO.class));
                cartItemDTO.setBookPrice(storeBookEntity.getEffectivePrice(bookEntity));
                cartItemDTO.setDiscountPercent(storeBookEntity.getDiscountPercent(bookEntity));
                cartItemDTO.setDiscountAmount(storeBookEntity.getDiscountAmount(bookEntity));
                totalPrice += cartItemDTO.getBookPrice() * quantity;
                cartItemDTOS.add(cartItemDTO);
            }

            cartDTO.setCartItem(cartItemDTOS);
            cartDTO.setTotalPrice(totalPrice);
            redisService.setTimeToLive(cartCacheKey(cartId), CART_CACHE_TTL_DAYS);
            return cartDTO;
        } catch (RedisConnectionFailureException | RedisSystemException ex) {
            logger.warn("Cannot read cart {} from Redis, fallback to database", cartId, ex);
            return null;
        }
    }

    private void syncCartToRedis(Long cartId) {
        try {
            CartEntity cartEntity = cartRepository.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));
            String cacheKey = cartCacheKey(cartId);
            redisService.delete(cacheKey);

            List<CartItemEntity> cartItems = cartItemRepository.findByCartId(cartId);
            for (CartItemEntity cartItem : cartItems) {
                if (cartItem.getStoreBookId() != null) {
                    redisService.hashSet(cacheKey, String.valueOf(cartItem.getStoreBookId()), cartItem.getQuantity());
                }
            }
            redisService.setTimeToLive(cacheKey, CART_CACHE_TTL_DAYS);
        } catch (RedisConnectionFailureException | RedisSystemException ex) {
            logger.warn("Cannot sync cart {} to Redis", cartId, ex);
        }
    }

    private CartItemDTO mapToCartItemDto(CartItemEntity cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setCartItemId(cartItem.getId());
        BookEntity bookEntity = bookRepository.findById(cartItem.getBookId()).orElse(null);
        StoreBookEntity storeBookEntity = cartItem.getStoreBookId() == null ? null
                : storeBookRepository.findById(cartItem.getStoreBookId()).orElse(null);
        if (storeBookEntity != null) {
            storeRepository.findById(storeBookEntity.getStoreId()).ifPresent(store -> {
                cartItemDTO.setStoreId(store.getId());
                cartItemDTO.setStoreName(store.getName());
            });
        }
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setBook(modelMapper.map(bookEntity, BookDTO.class));
        cartItemDTO.setBookPrice(cartItem.getBookPrice());
        if (storeBookEntity != null && bookEntity != null) {
            cartItemDTO.setDiscountPercent(storeBookEntity.getDiscountPercent(bookEntity));
            cartItemDTO.setDiscountAmount(storeBookEntity.getDiscountAmount(bookEntity));
        }
        return cartItemDTO;
    }

    private StoreBookEntity resolveStoreBook(Long storeBookId, Long bookId, Long storeId) {
        if (storeBookId != null) {
            return storeBookRepository.findById(storeBookId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        }
        if (storeId != null) {
            return storeBookRepository.findByStoreIdAndBookId(storeId, bookId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        }
        return storeBookRepository.findFirstByBookIdAndStockGreaterThanAndActiveTrueOrderByIdAsc(bookId, 0L)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM));
    }

    // Resolves which store_book row applies (3 possible lookup strategies), then re-fetches
    // that exact row WITH a pessimistic write lock by id. Only used where stock is actually
    // read/compared (addProductToCart) — deleteProductFromCart/decreaseProductFromCart never
    // read stock, so they intentionally keep using the unlocked resolveStoreBook via
    // findCartItem, to avoid blocking unrelated add-to-cart calls on the same book for no reason.
    private StoreBookEntity resolveStoreBookForUpdate(Long storeBookId, Long bookId, Long storeId) {
        StoreBookEntity resolved = resolveStoreBook(storeBookId, bookId, storeId);
        return storeBookRepository.findByIdForUpdate(resolved.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
    }

    private CartItemEntity findCartItem(Long cartId, Long storeBookId, Long bookId, Long storeId) {
        if (storeBookId != null || storeId != null) {
            StoreBookEntity storeBookEntity = resolveStoreBook(storeBookId, bookId, storeId);
            return cartItemRepository.findByCartIdAndStoreBookId(cartId, storeBookEntity.getId());
        }
        return cartItemRepository.findCartItemByCartIdAndBookId(cartId, bookId);
    }

    private String cartCacheKey(Long cartId) {
        return CART_CACHE_KEY_PREFIX + cartId;
    }

    private Integer toInteger(Object value) {
        if (value instanceof Integer integerValue) {
            return integerValue;
        }
        if (value instanceof Number numberValue) {
            return numberValue.intValue();
        }
        return Integer.valueOf(String.valueOf(value));
    }

    // Wraps a pessimistic-lock-guarded operation: a lock timeout or lock acquisition failure
    // (concurrent request holding the same Cart/StoreBook row) is a normal, expected outcome
    // under contention — translate it into a business exception with a clear retry message
    // instead of letting a Spring/JPA technical exception leak out to the client.
    private <T> T withLockErrorHandling(Supplier<T> action) {
        try {
            return action.get();
        } catch (PessimisticLockingFailureException | LockTimeoutException ex) {
            logger.warn("Lock conflict while modifying cart/stock", ex);
            throw new ResourceNotFoundException(MessageException.CART_PROCESSING_CONFLICT);
        }
    }

    private void withLockErrorHandling(Runnable action) {
        withLockErrorHandling(() -> {
            action.run();
            return null;
        });
    }
}
