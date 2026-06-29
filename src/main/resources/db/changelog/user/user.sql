--liquibase formatted sql

--changeset codex:001-create-user
CREATE TABLE `user` (
    id BIGINT AUTO_INCREMENT NOT NULL,
    code BINARY(16) NULL,
    created_time DATETIME NULL,
    updated_time DATETIME NULL,
    created_by_user_id BIGINT NULL,
    updated_by_user_id BIGINT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    first_name VARCHAR(255) NULL,
    last_name VARCHAR(255) NULL,
    username VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    email VARCHAR(255) NULL,
    avatar VARCHAR(255) NULL,
    phone_number VARCHAR(255) NULL,
    roles VARCHAR(255) NULL,
    device_token VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);
--rollback DROP TABLE `user`;

--changeset codex:002-create-roles
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT NOT NULL,
    role_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uk_roles_role_name UNIQUE (role_name)
);
--rollback DROP TABLE roles;
