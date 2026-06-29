--liquibase formatted sql

--changeset codex:001-create-refresh-token
CREATE TABLE refresh_token (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    token VARCHAR(255) NULL,
    user_id BIGINT NULL,
    expired_date DATETIME NULL,
    revoked BOOLEAN NOT NULL DEFAULT false,
    user_agent VARCHAR(255) NULL,
    ip_address VARCHAR(255) NULL,
    CONSTRAINT pk_refresh_token PRIMARY KEY (id)
);
--rollback DROP TABLE refresh_token;
