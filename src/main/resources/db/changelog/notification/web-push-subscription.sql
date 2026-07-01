--liquibase formatted sql

--changeset codex:001-create-web-push-subscription
CREATE TABLE web_push_subscription (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    user_id BIGINT NOT NULL,
    endpoint VARCHAR(512) NOT NULL,
    p256dh VARCHAR(255) NOT NULL,
    auth VARCHAR(255) NOT NULL,
    CONSTRAINT pk_web_push_subscription PRIMARY KEY (id),
    CONSTRAINT uk_web_push_subscription_endpoint UNIQUE (endpoint),
    CONSTRAINT fk_web_push_subscription_user FOREIGN KEY (user_id) REFERENCES `user` (id)
);
--rollback DROP TABLE web_push_subscription;
