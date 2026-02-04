package org.example.bookstore.service;


import jakarta.transaction.Transactional;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.BookEntity;
import org.example.bookstore.model.CartEntity;
import org.example.bookstore.model.CartItemEntity;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.CartDTO;
import org.example.bookstore.payload.CartItemDTO;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.CartItemRepository;
import org.example.bookstore.repository.CartRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.Long;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public CartService(CartRepository cartRepository, BookRepository bookRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public CartDTO addProductToCart(Long cartId, Long bookId, Integer quantity) {

        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));

        if (bookEntity.getStock() < quantity) {
            throw new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM);
        }

        CartItemEntity cartItemEntity = cartItemRepository.findCartItemByCartIdAndBookId(cartId, bookId);

        if (cartItemEntity != null) {
            cartItemEntity.setQuantity(cartItemEntity.getQuantity() + quantity);
            cartItemEntity.setBookPrice(cartItemEntity.getBookEntity().getPrice());
            cartItemRepository.save(cartItemEntity);
        } else {
            CartItemEntity newCartItemEntity = new CartItemEntity();
            newCartItemEntity.setCartEntity(cartEntity);
            newCartItemEntity.setBookEntity(bookEntity);
            newCartItemEntity.setQuantity(quantity);
            newCartItemEntity.setBookPrice(bookEntity.getPrice());
            cartItemRepository.save(newCartItemEntity);
        }

        bookEntity.setStock(bookEntity.getStock() - quantity);
        bookRepository.save(bookEntity);

        updateCartTotalPrice(cartEntity);
        cartRepository.save(cartEntity);

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cartId);
        cartDTO.setTotalPrice(cartEntity.getTotalPrice());
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        cartEntity.getCartItemEntities().forEach(cartItem1 -> {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setCartItemId(cartItem1.getId());
            cartItemDTO.setQuantity(cartItem1.getQuantity());
            cartItemDTO.setBook(modelMapper.map(cartItem1.getBookEntity(), BookDTO.class));
            cartItemDTO.setBookPrice(cartItem1.getBookPrice());
            cartItemDTOS.add(cartItemDTO);
        });
        cartDTO.setCartItem(cartItemDTOS);
        return cartDTO;
    }

    private void updateCartTotalPrice(CartEntity cartEntity) {
        long totalPrice = cartEntity.getCartItemEntities().stream()
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
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cartEntity.getId());
        cartDTO.setTotalPrice(cartEntity.getTotalPrice());
        if(cartEntity.getCartItemEntities().isEmpty()){
            logger.info("Empty cart!");
            cartDTO.setTotalPrice(0);
        }
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        cartEntity.getCartItemEntities().forEach(cartItem -> {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setCartItemId(cartItem.getId());
            cartItemDTO.setQuantity(cartItem.getQuantity());
            cartItemDTO.setBook(modelMapper.map(cartItem.getBookEntity(), BookDTO.class));
            cartItemDTO.setBookPrice(cartItem.getBookEntity().getPrice());
            cartItemDTOS.add(cartItemDTO);
        });
        cartDTO.setCartItem(cartItemDTOS);
        return cartDTO;
    }

    @Transactional
    public boolean deleteProductFromCart(Long cartId, Long bookId) {
        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));

        CartItemEntity cartItemEntity = cartItemRepository.findCartItemByCartIdAndBookId(cartId, bookId);

        if (cartItemEntity == null) {
            throw new ResourceNotFoundException(MessageException.CART_NO_FOUND_BOOK);
        }

        cartEntity.setTotalPrice((cartEntity.getTotalPrice()- cartItemEntity.getBookPrice())* cartItemEntity.getQuantity());
        cartRepository.save(cartEntity);
        BookEntity bookEntity = cartItemEntity.getBookEntity();
        bookEntity.setStock(bookEntity.getStock() + cartItemEntity.getQuantity());
        cartItemRepository.deleteCartItemByCartIdAndBookId(cartId, bookId);
        return true;

    }

    @Transactional
    public CartDTO decreaseProductFromCart(Long cartId, Long bookId) {
            CartEntity cartEntity = cartRepository.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));

            CartItemEntity cartItemEntity = cartItemRepository.findCartItemByCartIdAndBookId(cartId, bookId);
            if(cartItemEntity.getQuantity() == 1){
                deleteProductFromCart(cartId, bookId);
            } else {
                cartItemEntity.setQuantity(cartItemEntity.getQuantity() - 1);
                cartItemRepository.save(cartItemEntity);
            }
            CartDTO cartDTO = new CartDTO();
            cartDTO.setCartId(cartId);
            cartDTO.setTotalPrice(cartEntity.getTotalPrice());
            List<CartItemDTO> cartItemDTOS = new ArrayList<>();
            cartEntity.getCartItemEntities().forEach(cartItem1 -> {
                CartItemDTO cartItemDTO = new CartItemDTO();
                cartItemDTO.setCartItemId(cartItem1.getId());
                cartItemDTO.setQuantity(cartItem1.getQuantity());
                cartItemDTO.setBook(modelMapper.map(cartItem1.getBookEntity(), BookDTO.class));
                cartItemDTO.setBookPrice(cartItem1.getBookPrice());
                cartItemDTOS.add(cartItemDTO);
            });
            cartDTO.setCartItem(cartItemDTOS);
            return cartDTO;

        }
    }

