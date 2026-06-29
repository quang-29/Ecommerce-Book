--liquibase formatted sql

--changeset codex:001-create-review
CREATE TABLE review (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    content VARCHAR(255) NULL,
    rate_point INT NULL,
    created_at DATE NULL,
    book_id BIGINT NULL,
    user_id BIGINT NULL,
    CONSTRAINT pk_review PRIMARY KEY (id),
    CONSTRAINT fk_review_book FOREIGN KEY (book_id) REFERENCES book (id),
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES `user` (id)
);
--rollback DROP TABLE review;
