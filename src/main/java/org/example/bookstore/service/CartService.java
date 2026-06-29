package org.example.bookstore.service;


import jakarta.transaction.Transactional;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.BookEntity;
import org.example.bookstore.model.CartEntity;
import org.example.bookstore.model.CartItemEntity;
import org.example.bookstore.model.StoreBookEntity;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.CartDTO;
import org.example.bookstore.payload.CartItemDTO;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.CartItemRepository;
import org.example.bookstore.repository.CartRepository;
import org.example.bookstore.repository.StoreBookRepository;
import org.example.bookstore.service.Interface.BaseRedisService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Service;

import java.lang.Long;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final StoreBookRepository storeBookRepository;
    private final BaseRedisService<String, String, Object> redisService;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private static final String CART_CACHE_KEY_PREFIX = "cart:";
    private static final long CART_CACHE_TTL_DAYS = 30;

    public CartService(CartRepository cartRepository, BookRepository bookRepository, CartItemRepository cartItemRepository, StoreBookRepository storeBookRepository, BaseRedisService<String, String, Object> redisService) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.cartItemRepository = cartItemRepository;
        this.storeBookRepository = storeBookRepository;
        this.redisService = redisService;
    }

    public CartDTO addProductToCart(Long cartId, Long bookId, Integer quantity) {
        return addProductToCart(cartId, null, bookId, null, quantity);
    }

    public CartDTO addProductToCart(Long cartId, Long bookId, Long storeId, Integer quantity) {
        return addProductToCart(cartId, null, bookId, storeId, quantity);
    }

    @Transactional
    public CartDTO addProductToCart(Long cartId, Long storeBookId, Long bookId, Long storeId, Integer quantity) {

        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));
        StoreBookEntity storeBookEntity = resolveStoreBook(storeBookId, bookId, storeId);
        BookEntity bookEntity = storeBookEntity.getBookEntity();

        if (storeBookEntity.getStock() < quantity) {
            throw new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM);
        }

        CartItemEntity cartItemEntity = cartItemRepository.findByCartEntityIdAndStoreBookEntityId(cartId, storeBookEntity.getId());

        if (cartItemEntity != null) {
            cartItemEntity.setQuantity(cartItemEntity.getQuantity() + quantity);
            cartItemEntity.setBookPrice(storeBookEntity.getEffectivePrice());
            cartItemRepository.save(cartItemEntity);
        } else {
            CartItemEntity newCartItemEntity = new CartItemEntity();
            newCartItemEntity.setCartEntity(cartEntity);
            newCartItemEntity.setBookEntity(bookEntity);
            newCartItemEntity.setStoreBookEntity(storeBookEntity);
            newCartItemEntity.setQuantity(quantity);
            newCartItemEntity.setBookPrice(storeBookEntity.getEffectivePrice());
            cartItemRepository.save(newCartItemEntity);
        }

        updateCartTotalPrice(cartEntity);
        cartRepository.save(cartEntity);
        syncCartToRedis(cartId);

        return buildCartDtoFromDatabase(cartId);
    }

    private void updateCartTotalPrice(CartEntity cartEntity) {
        long totalPrice = cartItemRepository.findByCartEntityId(cartEntity.getId()).stream()
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
            List<CartItemDTO> cartItemDTOS = cart.getCartItemEntities().stream()
                    .map(p -> modelMapper.map(p.getBookEntity(), CartItemDTO.class)).collect(Collectors.toList());
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

        CartDTO cartDTO = buildCartDtoFromDatabase(cartEntity.getId());
        syncCartToRedis(cartEntity.getId());
        return cartDTO;
    }

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
        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));

        CartItemEntity cartItemEntity = findCartItem(cartId, storeBookId, bookId, storeId);

        if (cartItemEntity == null) {
            throw new ResourceNotFoundException(MessageException.CART_NO_FOUND_BOOK);
        }

        StoreBookEntity storeBookEntity = cartItemEntity.getStoreBookEntity();
        if (storeBookEntity != null) {
            cartItemRepository.deleteByCartEntityIdAndStoreBookEntityId(cartId, storeBookEntity.getId());
        } else {
            cartItemRepository.deleteCartItemByCartIdAndBookId(cartId, bookId);
        }
        cartEntity.setTotalPrice(Math.max(0, cartEntity.getTotalPrice() - cartItemEntity.getBookPrice() * cartItemEntity.getQuantity()));
        cartRepository.save(cartEntity);
        syncCartToRedis(cartId);
        return true;

    }

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
            CartEntity cartEntity = cartRepository.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));

            CartItemEntity cartItemEntity = findCartItem(cartId, storeBookId, bookId, storeId);
            if (cartItemEntity == null) {
                throw new ResourceNotFoundException(MessageException.CART_NO_FOUND_BOOK);
            }
            if(cartItemEntity.getQuantity() == 1){
                deleteProductFromCart(cartId, storeBookId, bookId, storeId);
            } else {
                cartItemEntity.setQuantity(cartItemEntity.getQuantity() - 1);
                cartItemRepository.save(cartItemEntity);
            }
            updateCartTotalPrice(cartEntity);
            cartRepository.save(cartEntity);
            syncCartToRedis(cartId);
            return buildCartDtoFromDatabase(cartId);

        }

    @Transactional
    public void clearCart(Long cartId) {
        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));
        cartItemRepository.deleteByCartEntityId(cartId);
        cartEntity.setTotalPrice(0);
        cartRepository.save(cartEntity);
        deleteCartCache(cartId);
    }

    private CartDTO buildCartDtoFromDatabase(Long cartId) {
        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cartId);

        List<CartItemEntity> cartItems = cartItemRepository.findByCartEntityId(cartId);
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
                BookEntity bookEntity = bookRepository.findById(storeBookEntity.getBookEntity().getId())
                        .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));

                CartItemDTO cartItemDTO = new CartItemDTO();
                CartItemEntity cartItemEntity = cartItemRepository.findByCartEntityIdAndStoreBookEntityId(cartId, storeBookId);
                cartItemDTO.setCartItemId(cartItemEntity == null ? null : cartItemEntity.getId());
                cartItemDTO.setStoreId(storeBookEntity.getStoreEntity().getId());
                cartItemDTO.setStoreName(storeBookEntity.getStoreEntity().getName());
                cartItemDTO.setQuantity(quantity);
                cartItemDTO.setBook(modelMapper.map(bookEntity, BookDTO.class));
                cartItemDTO.setBookPrice(storeBookEntity.getEffectivePrice());
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

            List<CartItemEntity> cartItems = cartItemRepository.findByCartEntityId(cartId);
            for (CartItemEntity cartItem : cartItems) {
                if (cartItem.getStoreBookEntity() != null) {
                    redisService.hashSet(cacheKey, String.valueOf(cartItem.getStoreBookEntity().getId()), cartItem.getQuantity());
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
        if (cartItem.getStoreBookEntity() != null) {
            cartItemDTO.setStoreId(cartItem.getStoreBookEntity().getStoreEntity().getId());
            cartItemDTO.setStoreName(cartItem.getStoreBookEntity().getStoreEntity().getName());
        }
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setBook(modelMapper.map(cartItem.getBookEntity(), BookDTO.class));
        cartItemDTO.setBookPrice(cartItem.getBookPrice());
        return cartItemDTO;
    }

    private StoreBookEntity resolveStoreBook(Long storeBookId, Long bookId, Long storeId) {
        if (storeBookId != null) {
            return storeBookRepository.findById(storeBookId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        }
        if (storeId != null) {
            return storeBookRepository.findByStoreEntityIdAndBookEntityId(storeId, bookId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        }
        return storeBookRepository.findFirstByBookEntityIdAndStockGreaterThanAndActiveTrueOrderByIdAsc(bookId, 0L)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM));
    }

    private CartItemEntity findCartItem(Long cartId, Long storeBookId, Long bookId, Long storeId) {
        if (storeBookId != null || storeId != null) {
            StoreBookEntity storeBookEntity = resolveStoreBook(storeBookId, bookId, storeId);
            return cartItemRepository.findByCartEntityIdAndStoreBookEntityId(cartId, storeBookEntity.getId());
        }
        return cartItemRepository.findCartItemByCartIdAndBookId(cartId, bookId);
    }

    private String cartCacheKey(Long cartId) {
        return CART_CACHE_KEY_PREFIX + cartId;
    }

    private void deleteCartCache(Long cartId) {
        try {
            redisService.delete(cartCacheKey(cartId));
        } catch (RedisConnectionFailureException | RedisSystemException ex) {
            logger.warn("Cannot delete cart {} from Redis", cartId, ex);
        }
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
    }
