package org.example.bookstore.controller;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.model.UserEntity;
import org.example.bookstore.payload.request.EditUser;
import org.example.bookstore.payload.request.UserUpdate;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.Long;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> getAllUsers(@RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer size,
                                                         @RequestParam(defaultValue = "username") String sortBy,
                                                         @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(userService.getAllUsers(page, size, sortBy, sortDirection));
    }

    @GetMapping("/mypage")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.username == authentication.name")
    public ResponseEntity<ServerResponseDto> getUserInfo() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(userService.getMyProfile(authentication.getName()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/update")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> updateUser(@RequestBody UserUpdate userUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = userService.getCurrentUserId(authentication);
        if(!currentUserId.equals(userUpdate.getId())) {
            throw new RuntimeException(MessageException.UNAUTHORIZED_ACTION.getMessage());
        }
        return ResponseEntity.ok(userService.updateUser(userUpdate));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> deleteUser(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @PutMapping("/likeBook")
    public ResponseEntity<ServerResponseDto> likedBook(@RequestParam Long userId,
                                                   @RequestParam Long bookId) {
        return ResponseEntity.ok(userService.likedBooks(userId, bookId));
    }

    @PutMapping("/unlikeBook")
    public ResponseEntity<ServerResponseDto> removeLikedBooks(@RequestParam Long userId,
                                                          @RequestParam Long bookId) {
        return ResponseEntity.ok(userService.removeLikedBooks(userId, bookId));
    }

    @GetMapping("/listBooksLikedByUser")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> listBooksLikedByUser(@RequestParam Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = userService.getCurrentUserId(authentication);
        if(!currentUserId.equals(userId)) {
            throw new RuntimeException(MessageException.UNAUTHORIZED_ACTION.getMessage());
        }
        return ResponseEntity.ok(userService.listBooksLikedByUser(userId));
    }

    @GetMapping("/getNumberOfUsers")
    public ResponseEntity<ServerResponseDto> getAllUser() {
        return ResponseEntity.ok(ServerResponseDto.success(userRepository.countUser()));

    }
    @PutMapping("/editUser")
    public ResponseEntity<ServerResponseDto> editUser(@RequestBody EditUser editUser) {
        return ResponseEntity.ok(userService.editUser(editUser));
    }

    @PutMapping("/changeAvatar/{userId}")
    public ResponseEntity<ServerResponseDto> changeAvatar(@PathVariable Long userId,
                                                      @RequestParam("file") MultipartFile file) throws FileUploadException {
        return ResponseEntity.ok(userService.changeAvatar(userId,file));
    }

    @GetMapping("/admin/avatar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ServerResponseDto> getAvatarAdmin() {
        Optional<UserEntity> user = userRepository.findByUsername("admin");
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ServerResponseDto.success(Map.of("avatarUrl", user.get().getAvatarUrl())));
    }
}
