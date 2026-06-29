--liquibase formatted sql

--changeset codex:001-create-payment
CREATE TABLE payment (
    id BIGINT AUTO_INCREMENT NOT NULL,
    amount BIGINT NOT NULL DEFAULT 0,
    fee_ship BIGINT NOT NULL DEFAULT 0,
    gateway TINYINT NULL,
    type TINYINT NULL,
    status TINYINT NULL,
    expire_at DATETIME NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    CONSTRAINT pk_payment PRIMARY KEY (id)
);
--rollback DROP TABLE payment;
