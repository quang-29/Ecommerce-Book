package org.example.bookstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;


public enum NotificationScope {
    SHOP("shop"), BUYER("buyer");

    private final String value;

    NotificationScope(String value) {
        this.value = value;
    }

    public String toString(){
        return value;
    }

    @JsonCreator
    public static NotificationScope fromString(String value) {
        for (NotificationScope scope : NotificationScope.values()) {
            if (scope.value.equalsIgnoreCase(value)) {
                return scope;
            }
        }
        throw new IllegalArgumentException("Invalid scope: " + value);
    }

}