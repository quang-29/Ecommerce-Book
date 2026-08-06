package org.example.bookstore.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebPushSubscriptionRequest {
    private String endpoint;
    private Keys keys;

    @Getter
    @Setter
    public static class Keys {
        private String p256dh;
        private String auth;
    }
}
