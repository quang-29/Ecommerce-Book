--liquibase formatted sql

--changeset codex:001-add-book-discount
ALTER TABLE book ADD COLUMN discount_percent INT NOT NULL DEFAULT 0;
--rollback ALTER TABLE book DROP COLUMN discount_percent;

--changeset codex:002-add-order-item-discount-snapshot
ALTER TABLE order_items ADD COLUMN discount_percent INT NOT NULL DEFAULT 0;
ALTER TABLE order_items ADD COLUMN discount_amount BIGINT NOT NULL DEFAULT 0;
--rollback ALTER TABLE order_items DROP COLUMN discount_amount;
--rollback ALTER TABLE order_items DROP COLUMN discount_percent;

--changeset codex:003-add-payment-discount
ALTER TABLE payment ADD COLUMN discount_amount BIGINT NOT NULL DEFAULT 0;
--rollback ALTER TABLE payment DROP COLUMN discount_amount;

--changeset codex:004-create-store-voucher
CREATE TABLE store_voucher (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    store_id BIGINT NOT NULL,
    voucher_code VARCHAR(255) NOT NULL,
    discount_percent INT NOT NULL DEFAULT 0,
    discount_amount BIGINT NOT NULL DEFAULT 0,
    min_order_amount BIGINT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT true,
    start_at DATETIME NULL,
    end_at DATETIME NULL,
    CONSTRAINT pk_store_voucher PRIMARY KEY (id),
    CONSTRAINT uk_store_voucher_store_code UNIQUE (store_id, voucher_code),
    CONSTRAINT fk_store_voucher_store FOREIGN KEY (store_id) REFERENCES store (id)
);
--rollback DROP TABLE store_voucher;
