package org.example.bookstore.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserDetails {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserDetails.class);

    public static CustomUserDetails getCurrentUser(){
        if(SecurityContextHolder.getContext() != null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null && authentication.isAuthenticated()){
                Object principal = authentication.getPrincipal();

                if(principal instanceof CustomUserDetails){
                    return (CustomUserDetails) principal;
                } else {
                    LOGGER.info("Invalid principle class, expect CustomUserDetails class.");
                }
            }
        }
        return null;
    }
}
