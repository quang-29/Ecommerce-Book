--liquibase formatted sql

--changeset codex:001-create-store
CREATE TABLE store (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    store_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(32) NULL,
    email VARCHAR(255) NULL,
    province_id BIGINT NULL,
    district_id BIGINT NULL,
    ward_id VARCHAR(64) NULL,
    address_detail VARCHAR(500) NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT pk_store PRIMARY KEY (id)
);
--rollback DROP TABLE store;

--changeset codex:002-create-store-book
CREATE TABLE store_book (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    store_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    stock BIGINT NOT NULL DEFAULT 0,
    price BIGINT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT pk_store_book PRIMARY KEY (id),
    CONSTRAINT uk_store_book_store_book UNIQUE (store_id, book_id),
    CONSTRAINT fk_store_book_store FOREIGN KEY (store_id) REFERENCES store (id),
    CONSTRAINT fk_store_book_book FOREIGN KEY (book_id) REFERENCES book (id)
);
--rollback DROP TABLE store_book;

--changeset codex:003-add-store-book-to-cart-and-order-items
ALTER TABLE cart_items ADD COLUMN store_book_id BIGINT NULL;
ALTER TABLE order_items ADD COLUMN store_book_id BIGINT NULL;
ALTER TABLE cart_items
    ADD CONSTRAINT fk_cart_items_store_book FOREIGN KEY (store_book_id) REFERENCES store_book (id);
ALTER TABLE order_items
    ADD CONSTRAINT fk_order_items_store_book FOREIGN KEY (store_book_id) REFERENCES store_book (id);
--rollback ALTER TABLE order_items DROP FOREIGN KEY fk_order_items_store_book;
--rollback ALTER TABLE cart_items DROP FOREIGN KEY fk_cart_items_store_book;
--rollback ALTER TABLE order_items DROP COLUMN store_book_id;
--rollback ALTER TABLE cart_items DROP COLUMN store_book_id;

--changeset codex:004-migrate-book-stock-to-default-store
INSERT INTO store (id, store_name, address_detail, active, is_deleted)
VALUES (1, 'Default Store', '144 Chien Thang', true, false)
ON DUPLICATE KEY UPDATE store_name = store_name;

INSERT INTO store_book (store_id, book_id, stock, price, active, is_deleted)
SELECT 1, b.id, COALESCE(b.stock, 0), b.price, true, false
FROM book b
WHERE NOT EXISTS (
    SELECT 1
    FROM store_book sb
    WHERE sb.store_id = 1 AND sb.book_id = b.id
);
--rollback DELETE FROM store_book WHERE store_id = 1;
--rollback DELETE FROM store WHERE id = 1;
