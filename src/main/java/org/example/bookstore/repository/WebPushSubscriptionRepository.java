package org.example.bookstore.repository;

import org.example.bookstore.model.WebPushSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebPushSubscriptionRepository extends CrudRepository<WebPushSubscription, Long> {
    List<WebPushSubscription> findAllByUserId(Long userId);

    Optional<WebPushSubscription> findByEndpoint(String endpoint);

    void deleteByEndpoint(String endpoint);
}
