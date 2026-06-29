--liquibase formatted sql

--changeset codex:001-create-orders
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    user_id BIGINT NULL,
    create_at DATETIME NULL,
    estimated_delivery_date DATETIME NULL,
    address_id BIGINT NULL,
    payment_id BIGINT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES `user` (id),
    CONSTRAINT fk_orders_user_address FOREIGN KEY (address_id) REFERENCES user_address (id),
    CONSTRAINT fk_orders_payment FOREIGN KEY (payment_id) REFERENCES payment (id)
);
--rollback DROP TABLE orders;

--changeset codex:002-create-order-items
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    book_id BIGINT NULL,
    order_id BIGINT NULL,
    quantity INT NULL,
    product_price BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_order_items PRIMARY KEY (id),
    CONSTRAINT fk_order_items_book FOREIGN KEY (book_id) REFERENCES book (id),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id)
);
--rollback DROP TABLE order_items;
