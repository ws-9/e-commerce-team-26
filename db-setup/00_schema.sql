CREATE TABLE users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    name     VARCHAR(255)        NOT NULL,
    address  TEXT                NOT NULL
);

CREATE TABLE admins
(
    user_id BIGINT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    enabled BOOLEAN DEFAULT TRUE NOT NULL
);

CREATE TABLE sellers
(
    user_id       BIGINT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    business_name VARCHAR(255)         NOT NULL,
    description   TEXT,
    verified      BOOLEAN DEFAULT TRUE NOT NULL,
    taxId         VARCHAR(100)         NOT NULL,
    contact_email VARCHAR(255)         NOT NULL
);