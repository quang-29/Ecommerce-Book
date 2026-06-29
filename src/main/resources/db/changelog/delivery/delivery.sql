--liquibase formatted sql

--changeset codex:001-create-delivery-type
CREATE TABLE delivery_type (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    price DECIMAL(19, 2) NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_delivery_type PRIMARY KEY (id)
);
--rollback DROP TABLE delivery_type;
