package org.example.bookstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.example.bookstore.exception.AppException;

public enum OauthProvider {
    GOOGLE("google");

    private final String value;

    OauthProvider(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OauthProvider fromString(String value) {
        for (OauthProvider provider : OauthProvider.values()) {
            if (provider.value.equalsIgnoreCase(value)) {
                return provider;
            }
        }
        throw new RuntimeException("Invalid provider: " + value);
    }
    public String toString() {
        return value;
    }
}
