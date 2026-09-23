package org.example.bookstore.service;

import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.enums.Roles;
import org.example.bookstore.repository.CartRepository;
import org.example.bookstore.repository.OrderRepository;
import org.example.bookstore.security.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.example.bookstore.security.CurrentUserDetails.getCurrentUser;

@Slf4j
@Service(value = "authorizationService")
public class PreAuthorizeService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public PreAuthorizeService(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    public boolean isAdmin(){
        CustomUserDetails customUserDetails = getCurrentUser();
        if (customUserDetails == null){
            return false;
        }
        Roles roles = customUserDetails.getRoles();
        return roles == Roles.ADMIN;
    }

    public boolean isUser(){
        CustomUserDetails customUserDetails = getCurrentUser();
        if (customUserDetails == null){
            return false;
        }
        Roles roles = customUserDetails.getRoles();
        return roles == Roles.USER;
    }

    public boolean isMySelf(Long userId){
        CustomUserDetails customUserDetails = getCurrentUser();
        if (customUserDetails == null){
            return false;
        }
        return Objects.equals(userId, customUserDetails.getUserId());
    }

    public boolean isOrderOwner(Long orderId){
        CustomUserDetails customUserDetails = getCurrentUser();
        if (customUserDetails == null){
            return false;
        }
        return orderRepository.findById(orderId)
                .map(order -> Objects.equals(order.getUserId(), customUserDetails.getUserId()))
                .orElse(false);
    }

    public boolean isCartOwner(Long cartId){
        CustomUserDetails customUserDetails = getCurrentUser();
        if (customUserDetails == null){
            return false;
        }
        return cartRepository.findById(cartId)
                .map(cart -> Objects.equals(cart.getUserId(), customUserDetails.getUserId()))
                .orElse(false);
    }
}
