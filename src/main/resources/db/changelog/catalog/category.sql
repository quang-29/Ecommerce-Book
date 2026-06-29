--liquibase formatted sql

--changeset codex:001-create-category
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    category_name VARCHAR(255) NULL,
    category_img VARCHAR(255) NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);
--rollback DROP TABLE category;
