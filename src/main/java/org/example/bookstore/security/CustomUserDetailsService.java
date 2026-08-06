package org.example.bookstore.security;

import lombok.AllArgsConstructor;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.model.UserEntity;
import org.example.bookstore.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException(MessageException.USER_NOT_FOUND.getMessage());
        }

        UserEntity user = userRepository.findUserByUsername(username)
                .or(() -> username.contains("@") ? userRepository.findUserByEmail(username) : Optional.empty())
                .orElseThrow(() -> new UsernameNotFoundException(MessageException.USER_NOT_FOUND.getMessage()));

        return CustomUserDetails.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getFirstName())
                .phone(user.getPhoneNumber())
                .roles(user.getRoles())
                .password(user.getPassword())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
