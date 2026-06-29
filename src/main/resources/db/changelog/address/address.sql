--liquibase formatted sql

--changeset codex:001-create-provinces
CREATE TABLE provinces (
    id INT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_provinces PRIMARY KEY (id)
);
--rollback DROP TABLE provinces;

--changeset codex:002-create-districts
CREATE TABLE districts (
    id INT NOT NULL,
    province_id INT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_districts PRIMARY KEY (id)
);
--rollback DROP TABLE districts;

--changeset codex:003-create-wards
CREATE TABLE wards (
    id VARCHAR(255) NOT NULL,
    district_id INT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_wards PRIMARY KEY (id)
);
--rollback DROP TABLE wards;

--changeset codex:004-create-user-address
CREATE TABLE user_address (
    id BIGINT AUTO_INCREMENT NOT NULL,
    province_id INT NULL,
    district_id INT NULL,
    ward_id VARCHAR(255) NULL,
    detail VARCHAR(255) NULL,
    username VARCHAR(255) NULL,
    receiver_name VARCHAR(255) NULL,
    phone_number VARCHAR(255) NULL,
    is_primary BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT pk_user_address PRIMARY KEY (id),
    CONSTRAINT fk_user_address_province FOREIGN KEY (province_id) REFERENCES provinces (id),
    CONSTRAINT fk_user_address_district FOREIGN KEY (district_id) REFERENCES districts (id),
    CONSTRAINT fk_user_address_ward FOREIGN KEY (ward_id) REFERENCES wards (id)
);
--rollback DROP TABLE user_address;
