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
    business_name VARCHAR(255) UNIQUE  NOT NULL,
    description   TEXT,
    verified      BOOLEAN DEFAULT TRUE NOT NULL,
    tax_id        VARCHAR(100) UNIQUE  NOT NULL,
    contact_email VARCHAR(255) UNIQUE  NOT NULL
);

CREATE TABLE categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE products
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    stock_qty   INT            NOT NULL DEFAULT 0,
    seller_id   BIGINT         NOT NULL REFERENCES sellers (user_id) ON DELETE CASCADE,
    category_id BIGINT         NOT NULL REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE orders
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    date             TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id          BIGINT         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    status           VARCHAR(100)   NOT NULL,
    total            DECIMAL(10, 2) NOT NULL,
    shipping_address TEXT           NOT NULL
);

CREATE TABLE order_items
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    qty               INT            NOT NULL,
    price_at_purchase DECIMAL(10, 2) NOT NULL,
    order_id          BIGINT         NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id        BIGINT         REFERENCES products (id) ON DELETE SET NULL
);