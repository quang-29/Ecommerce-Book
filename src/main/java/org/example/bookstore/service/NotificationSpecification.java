package org.example.bookstore.service;

import jakarta.persistence.criteria.Join;
import org.example.bookstore.model.Notifications;
import org.example.bookstore.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class NotificationSpecification {
    public static Specification<Notifications> hasUserId(String userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null || userId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Notifications, User> usersJoin = root.join("users");
            return criteriaBuilder.equal(usersJoin.get("id"), UUID.fromString(userId));
        };
    }

    public static Specification<Notifications> searchByKeyword(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + search.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("context")), "%" + search.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Notifications> filtersNotification(String userId, String search){
        return Specification.where(hasUserId(userId))
                .and(searchByKeyword(search));
    }
}
