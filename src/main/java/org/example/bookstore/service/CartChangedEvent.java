package org.example.bookstore.service;

// Published inside a cart-mutating transaction instead of syncing Redis directly,
// so the actual cache write only happens in the AFTER_COMMIT listener below —
// if the transaction rolls back, the event is simply dropped and Redis is never touched.
public record CartChangedEvent(Long cartId) {
}
