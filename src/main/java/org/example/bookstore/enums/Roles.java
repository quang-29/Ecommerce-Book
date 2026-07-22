package org.example.bookstore.enums;

public enum Roles {
    ADMIN("ADMIN"),
    USER("USER"),
    SHOP_OWNER("SHOP_OWNER");

    private final String name;

    Roles(String name) {
        this.name = name;
    }
    public String getRole() {
        return name;
    }
}
