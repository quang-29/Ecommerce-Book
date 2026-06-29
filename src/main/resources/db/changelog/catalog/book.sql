--liquibase formatted sql

--changeset codex:001-create-book
CREATE TABLE book (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    title VARCHAR(255) NULL,
    category_id BIGINT NULL,
    author_id BIGINT NULL,
    price BIGINT NOT NULL,
    average_rating DOUBLE NULL,
    book_description TEXT NULL,
    language VARCHAR(255) NULL,
    image_path VARCHAR(255) NULL,
    isbn VARCHAR(255) NOT NULL,
    page INT NULL,
    publisher VARCHAR(255) NOT NULL,
    reprint INT NULL,
    stock BIGINT NULL,
    sold BIGINT NOT NULL,
    published_date DATE NOT NULL,
    CONSTRAINT pk_book PRIMARY KEY (id),
    CONSTRAINT fk_book_category FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_book_author FOREIGN KEY (author_id) REFERENCES author (id)
);
--rollback DROP TABLE book;

--changeset codex:002-create-users-liked-books
CREATE TABLE users_liked_books (
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    CONSTRAINT pk_users_liked_books PRIMARY KEY (user_id, book_id),
    CONSTRAINT fk_users_liked_books_user FOREIGN KEY (user_id) REFERENCES `user` (id),
    CONSTRAINT fk_users_liked_books_book FOREIGN KEY (book_id) REFERENCES book (id)
);
--rollback DROP TABLE users_liked_books;
