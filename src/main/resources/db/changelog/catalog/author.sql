--liquibase formatted sql

--changeset codex:001-create-author
CREATE TABLE author (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    author_name VARCHAR(255) NULL,
    biography VARCHAR(255) NULL,
    email VARCHAR(255) NULL,
    website VARCHAR(255) NULL,
    dob DATETIME NULL,
    country VARCHAR(255) NULL,
    image_url VARCHAR(255) NULL,
    CONSTRAINT pk_author PRIMARY KEY (id)
);
--rollback DROP TABLE author;
