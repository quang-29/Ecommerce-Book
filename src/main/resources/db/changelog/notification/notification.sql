--liquibase formatted sql

--changeset codex:001-create-notifications
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    content VARCHAR(255) NULL,
    scope VARCHAR(255) NULL,
    item_count INT NOT NULL DEFAULT 0,
    thumbnail_url VARCHAR(255) NULL,
    receiver_id BIGINT NULL,
    redirect_url VARCHAR(255) NULL,
    is_read BOOLEAN NOT NULL DEFAULT false,
    created_at DATETIME NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id),
    CONSTRAINT fk_notifications_receiver FOREIGN KEY (receiver_id) REFERENCES `user` (id)
);
--rollback DROP TABLE notifications;
