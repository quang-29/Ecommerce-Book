--liquibase formatted sql

--changeset codex:001-create-carts
CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    user_id BIGINT NULL,
    total_price BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_carts PRIMARY KEY (id),
    CONSTRAINT fk_carts_user FOREIGN KEY (user_id) REFERENCES `user` (id)
);
--rollback DROP TABLE carts;

--changeset codex:002-create-cart-items
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    cart_id BIGINT NULL,
    book_id BIGINT NULL,
    quantity INT NULL,
    book_price BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_cart_items PRIMARY KEY (id),
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts (id),
    CONSTRAINT fk_cart_items_book FOREIGN KEY (book_id) REFERENCES book (id)
);
--rollback DROP TABLE cart_items;
