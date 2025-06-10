package org.example.bookstore.service;

import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.AppException;
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
import org.example.bookstore.repository.RoleRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.Interface.CartService;
import org.example.bookstore.service.Interface.UserService;
import org.example.bookstore.utils.FileUploadUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CloudinaryServiceImpl cloudinaryServiceImpl;

    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<User> pageUsers = userRepository.findAll(pageDetails);
        List<UserDTO> userDTOs = pageUsers.getContent().stream()
                .map(user -> {
                    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                    return userDTO;
                }).toList();

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userDTOs);
        userResponse.setPageNumber(pageUsers.getNumber());
        userResponse.setPageSize(pageUsers.getSize());
        userResponse.setTotalElements(pageUsers.getTotalElements());
        userResponse.setTotalPages(pageUsers.getTotalPages());
        userResponse.setLastPage(pageUsers.isLast());
        return userResponse;

    }

    @Override
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

        List<CartItemDTO> cartItemDTOS = user.getCart().getCartItems().stream()
                .map(item -> modelMapper.map(item.getBook(), CartItemDTO.class)).collect(Collectors.toList());
        userDTO.setCart(cart);

        userDTO.getCart().setCartItem(cartItemDTOS);
        return userDTO;
    }

    @Override
    public UserDTO updateUser(UserUpdate userUpdate) {
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
        return userDTO;
    }

    @Override
    public String deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<CartItem> cartItems = user.getCart().getCartItems();
        Cart cart = user.getCart();

        cartItems.forEach(item -> {

            UUID bookId = item.getBook().getId();

            cartService.deleteProductFromCart(cart.getId(), bookId);
        });

        userRepository.delete(user);
        return "User with userId " + userId + " deleted successfully!!!";
    }

    @Override
    public UserDTO getMyProfile(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public String likedBooks(UUID userId, UUID bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        Set<Book> likedBooks = user.getLikedBooks();

        if (!likedBooks.contains(book)) {
            likedBooks.add(book);
            userRepository.save(user);
            return "Like book successfully!!!";
        }
        return "Book already liked!";
    }

    @Override
    public String removeLikedBooks(UUID userId, UUID bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        Set<Book> likedBooks = user.getLikedBooks();

        if (likedBooks.contains(book)) {
            likedBooks.remove(book);
            userRepository.save(user);
            return "Dislike book successfully!!!";
        }
        return "Book was not liked before!";
    }

    @Override
    public Set<BookDTO> listBooksLikedByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return user.getLikedBooks().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toSet());
    }

    @Override
    public UUID getCurrentUserId(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }

    @Override
    public boolean editUser(EditUser editUser) {
        User user = userRepository.findById(editUser.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setFirstName(editUser.getFirstName());
        user.setLastName(editUser.getLastName());
        user.setPhoneNumber(editUser.getPhoneNumber());
        user.setEmail(editUser.getEmail());
        userRepository.save(user);
        return true;
    }

    @Override
    public ChangeAvatar changeAvatar(UUID userId, MultipartFile file) {
        ChangeAvatar changeAvatar = new ChangeAvatar();
        try {
            Optional<User> userFound = userRepository.findById(userId);
            if (userFound.isEmpty()) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
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
        return changeAvatar;
    }


}
