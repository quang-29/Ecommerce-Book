--liquibase formatted sql

--changeset codex:001-create-room
CREATE TABLE room (
    id BIGINT AUTO_INCREMENT NOT NULL,
    room_id VARCHAR(255) NULL,
    user_id BIGINT NULL,
    user_avatar VARCHAR(255) NULL,
    user_name VARCHAR(255) NULL,
    created_at DATETIME NULL,
    CONSTRAINT pk_room PRIMARY KEY (id),
    CONSTRAINT uk_room_room_id UNIQUE (room_id)
);
--rollback DROP TABLE room;

--changeset codex:002-create-message
CREATE TABLE message (
    id BIGINT AUTO_INCREMENT NOT NULL,
    room_id VARCHAR(255) NULL,
    sender VARCHAR(255) NULL,
    sender_url VARCHAR(255) NULL,
    content VARCHAR(255) NULL,
    sent_at DATETIME NULL,
    CONSTRAINT pk_message PRIMARY KEY (id)
);
--rollback DROP TABLE message;
