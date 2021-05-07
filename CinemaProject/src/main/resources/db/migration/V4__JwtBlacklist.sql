CREATE TABLE jwt_blacklist (
    id BIGINT NOT NULL,
    token VARCHAR(255) UNIQUE,
    PRIMARY KEY (id)
);

