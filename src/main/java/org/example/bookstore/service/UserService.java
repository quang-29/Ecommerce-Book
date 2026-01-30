package org.example.bookstore.service;

import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.*;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.CartDTO;
import org.example.bookstore.payload.CartItemDTO;
import org.example.bookstore.payload.UserDTO;
import org.example.bookstore.payload.request.ChangeAvatar;
import org.example.bookstore.payload.request.EditUser;
import org.example.bookstore.payload.request.UserUpdate;
import org.example.bookstore.payload.response.CloudinaryResponse;
import org.example.bookstore.payload.response.UserResponse;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.Interface.CartService;
import org.example.bookstore.utils.FileUploadUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final BookRepository bookRepository;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, CartService cartService, BookRepository bookRepository, CloudinaryServiceImpl cloudinaryServiceImpl) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.cartService = cartService;
        this.bookRepository = bookRepository;
        this.cloudinaryServiceImpl = cloudinaryServiceImpl;
    }

    public ServerResponseDto getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<UserDTO> pageUsers = userRepository.findAll(pageDetails).map(user -> modelMapper.map(user, UserDTO.class));
        return ServerResponseDto.success(pageUsers);

    }

    public ServerResponseDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(MessageException.USER_NOT_FOUND.getMessage()));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

        List<CartItemDTO> cartItemDTOS = user.getCart().getCartItems().stream()
                .map(item -> modelMapper.map(item.getBook(), CartItemDTO.class)).collect(Collectors.toList());
        userDTO.setCart(cart);

        userDTO.getCart().setCartItem(cartItemDTOS);
        return ServerResponseDto.success(userDTO);
    }
    
    public ServerResponseDto updateUser(UserUpdate userUpdate) {
        User user = userRepository.findUserByUsername(userUpdate.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        user.setPhoneNumber(userUpdate.getPhoneNumber());
        userRepository.save(user);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);
        List<CartItemDTO> cartItemDTOS = user.getCart().getCartItems().stream()
                .map(item -> modelMapper.map(item.getBook(), CartItemDTO.class)).collect(Collectors.toList());
        userDTO.setCart(cart);
        userDTO.getCart().setCartItem(cartItemDTOS);
        return ServerResponseDto.success(userDTO);
    }

    public ServerResponseDto deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<CartItem> cartItems = user.getCart().getCartItems();
        Cart cart = user.getCart();

        cartItems.forEach(item -> {

            UUID bookId = item.getBook().getId();

            cartService.deleteProductFromCart(cart.getId(), bookId);
        });

        userRepository.delete(user);
        return ServerResponseDto.success("Delete user successfully");
    }

    public ServerResponseDto getMyProfile(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException(MessageException.USER_NOT_FOUND.getMessage()));
        return ServerResponseDto.success(modelMapper.map(user, UserDTO.class));
    }
    
    public ServerResponseDto likedBooks(UUID userId, UUID bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(MessageException.USER_NOT_FOUND.getMessage()));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException(MessageException.BOOK_NOT_FOUND.getMessage()));

        Set<Book> likedBooks = user.getLikedBooks();

        if (!likedBooks.contains(book)) {
            likedBooks.add(book);
            userRepository.save(user);
            return ServerResponseDto.success("Like book successfully");
        }
        return ServerResponseDto.success("Book already liked!");
    }

    public ServerResponseDto removeLikedBooks(UUID userId, UUID bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(MessageException.USER_NOT_FOUND.getMessage()));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException(MessageException.BOOK_NOT_FOUND.getMessage()));

        Set<Book> likedBooks = user.getLikedBooks();
        if (likedBooks.contains(book)) {
            likedBooks.remove(book);
            userRepository.save(user);
            return ServerResponseDto.success("Dislike book successfully!!!");
        }
        return ServerResponseDto.success("Book was not liked before!");
    }

    public ServerResponseDto listBooksLikedByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(MessageException.USER_NOT_FOUND.getMessage()));

        return ServerResponseDto.success(user.getLikedBooks().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toSet()));
    }

    public UUID getCurrentUserId(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException(MessageException.USER_NOT_FOUND.getMessage()));
        return user.getId();
    }

    public ServerResponseDto editUser(EditUser editUser) {
        User user = userRepository.findById(editUser.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setFirstName(editUser.getFirstName());
        user.setLastName(editUser.getLastName());
        user.setPhoneNumber(editUser.getPhoneNumber());
        user.setEmail(editUser.getEmail());
        userRepository.save(user);
        return ServerResponseDto.success("Edit user successfully");
    }

    public ServerResponseDto changeAvatar(UUID userId, MultipartFile file) {
        ChangeAvatar changeAvatar = new ChangeAvatar();
        try {
            Optional<User> userFound = userRepository.findById(userId);
            if (userFound.isEmpty()) {
                throw new RuntimeException(MessageException.USER_NOT_FOUND.getMessage());
            }
            User user = userFound.get();
            FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
            final String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
            final CloudinaryResponse response = cloudinaryServiceImpl.uploadFile(file, fileName);
            user.setAvatarUrl(response.getUrl());
            userRepository.save(user);
            changeAvatar.setSuccess(true);
            changeAvatar.setUrl(user.getAvatarUrl());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return ServerResponseDto.success(changeAvatar);
    }


}
