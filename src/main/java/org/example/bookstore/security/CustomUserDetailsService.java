package org.example.bookstore.security;

import lombok.AllArgsConstructor;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.enums.MessageException;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.UserEntity;
import org.example.bookstore.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException(MessageException.USER_NOT_FOUND.getMessage()));

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoles().getRole()));

        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                authorities
        );
    }
}
