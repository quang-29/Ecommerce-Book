package org.example.bookstore.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
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
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.CartItemRepository;
import org.example.bookstore.repository.CartRepository;
import org.example.bookstore.repository.NotificationRepository;
import org.example.bookstore.repository.OrderItemRepository;
import org.example.bookstore.repository.OrderRepository;
import org.example.bookstore.repository.ReviewRepository;
import org.example.bookstore.repository.UserLikedBookRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.cache.UserCacheDTO;
import org.example.bookstore.service.cache.UserCacheService;
import org.example.bookstore.utils.FileUploadUtil;
import org.modelmapper.ModelMapper;
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
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;
    private final NotificationRepository notificationRepository;
    private final UserLikedBookRepository userLikedBookRepository;
    private final BookRepository bookRepository;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final UserCacheService userCacheService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, CartService cartService, CartRepository cartRepository, CartItemRepository cartItemRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ReviewRepository reviewRepository, NotificationRepository notificationRepository, UserLikedBookRepository userLikedBookRepository, BookRepository bookRepository, CloudinaryServiceImpl cloudinaryServiceImpl, UserCacheService userCacheService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.reviewRepository = reviewRepository;
        this.notificationRepository = notificationRepository;
        this.userLikedBookRepository = userLikedBookRepository;
        this.bookRepository = bookRepository;
        this.cloudinaryServiceImpl = cloudinaryServiceImpl;
        this.userCacheService = userCacheService;
    }

    public ServerResponseDto getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<UserDTO> pageUsers = userRepository.findAll(pageDetails).map(user -> modelMapper.map(user, UserDTO.class));
        return ServerResponseDto.success(pageUsers);

    }

    public ServerResponseDto getUserById(Long userId) {
        UserDTO userDTO = userCacheService.getUser(userId)
                .map(this::toUserDTO)
                .orElseGet(() -> {
                    UserEntity user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));
                    userCacheService.cache(user);
                    return modelMapper.map(user, UserDTO.class);
                });

        userDTO.setCart(cartService.getCartByUserId(userId.toString()));
        return ServerResponseDto.success(userDTO);
    }

    private UserDTO toUserDTO(UserCacheDTO cached) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(cached.getUserId());
        userDTO.setUsername(cached.getUsername());
        userDTO.setFirstName(cached.getFirstName());
        userDTO.setLastName(cached.getLastName());
        userDTO.setPhoneNumber(cached.getPhoneNumber());
        userDTO.setEmail(cached.getEmail());
        userDTO.setAvatarUrl(cached.getAvatarUrl());
        return userDTO;
    }
    
    public ServerResponseDto updateUser(UserUpdate userUpdate) {
        UserEntity user = userRepository.findUserByUsername(userUpdate.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));

        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        user.setPhoneNumber(userUpdate.getPhoneNumber());
        userRepository.save(user);
        userCacheService.cache(user);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        CartEntity cartEntity = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));
        CartDTO cart = modelMapper.map(cartEntity, CartDTO.class);
        List<CartItemDTO> cartItemDTOS = cartItemRepository.findByCartId(cartEntity.getId()).stream()
                .map(item -> modelMapper.map(bookRepository.findById(item.getBookId()).orElse(null), CartItemDTO.class))
                .collect(Collectors.toList());
        userDTO.setCart(cart);
        userDTO.getCart().setCartItem(cartItemDTOS);
        return ServerResponseDto.success(userDTO);
    }

    // Deleting a user used to cascade (via JPA cascade=ALL/orphanRemoval on UserEntity's
    // relation fields) to the cart, orders, reviews and notifications owned by that user —
    // all of those tables have a real FK constraint back to `user` with no ON DELETE CASCADE,
    // so this cleanup has to happen explicitly now, in FK-safe order, before userRepository.delete.
    public ServerResponseDto deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));

        cartRepository.findByUserId(user.getId()).ifPresent(cartEntity -> {
            List<CartItemEntity> cartItemEntities = cartItemRepository.findByCartId(cartEntity.getId());
            cartItemEntities.forEach(item -> cartService.deleteProductFromCart(cartEntity.getId(), item.getBookId()));
            cartRepository.delete(cartEntity);
        });

        List<OrderEntity> orders = orderRepository.findAllByUserId(user.getId());
        for (OrderEntity order : orders) {
            orderItemRepository.deleteAll(orderItemRepository.findByOrderId(order.getId()));
        }
        orderRepository.deleteAll(orders);

        reviewRepository.deleteAll(reviewRepository.findAllReviewsByUserId(user.getId()));

        notificationRepository.deleteAll(notificationRepository.findAllByReceiverId(user.getId()));

        userRepository.delete(user);
        userCacheService.evict(user);
        return ServerResponseDto.success("Delete user successfully");
    }

    public ServerResponseDto getMyProfile(String username) {
        UserEntity user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));
        userCacheService.cache(user);
        return ServerResponseDto.success(modelMapper.map(user, UserDTO.class));
    }

    public ServerResponseDto actionBooks(Long userId, Long bookId, boolean isLike){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        if(isLike) {
            if (!userLikedBookRepository.existsByUserIdAndBookId(user.getId(), bookEntity.getId())) {
                userLikedBookRepository.save(new UserLikedBookEntity(user.getId(), bookEntity.getId()));
            }
            return ServerResponseDto.success("Like book successfully!!!");
        } else {
            userLikedBookRepository.deleteByUserIdAndBookId(user.getId(), bookEntity.getId());
            return ServerResponseDto.success("Dislike book successfully!!!");
        }
    }

    public ServerResponseDto listBooksLikedByUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));

        List<Long> likedBookIds = userLikedBookRepository.findByUserId(user.getId()).stream()
                .map(UserLikedBookEntity::getBookId)
                .toList();
        return ServerResponseDto.success(bookRepository.findAllById(likedBookIds).stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toSet()));
    }

    public Long getCurrentUserId(Authentication authentication) {
        String username = authentication.getName();
        UserEntity user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));
        userCacheService.cache(user);
        return user.getId();
    }

    public ServerResponseDto editUser(EditUser editUser) {
        UserEntity user = userRepository.findById(editUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));
        user.setFirstName(editUser.getFirstName());
        user.setLastName(editUser.getLastName());
        user.setPhoneNumber(editUser.getPhoneNumber());
        user.setEmail(editUser.getEmail());
        userRepository.save(user);
        userCacheService.cache(user);
        return ServerResponseDto.success("Edit user successfully");
    }

    public ServerResponseDto changeAvatar(Long userId, MultipartFile file) throws FileUploadException {
        ChangeAvatar changeAvatar = new ChangeAvatar();
        try {
            Optional<UserEntity> userFound = userRepository.findById(userId);
            if (userFound.isEmpty()) {
                throw new ResourceNotFoundException(MessageException.USER_NOT_FOUND);
            }
            UserEntity user = userFound.get();
            FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
            final String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
            final CloudinaryResponse response = cloudinaryServiceImpl.uploadFile(file, fileName);
            user.setAvatarUrl(response.getUrl());
            userRepository.save(user);
            userCacheService.cache(user);
            changeAvatar.setSuccess(true);
            changeAvatar.setUrl(user.getAvatarUrl());
        } catch (FileUploadException ex) {
            throw new FileUploadException(ex.getMessage());
        }
        return ServerResponseDto.success(changeAvatar);
    }


}
