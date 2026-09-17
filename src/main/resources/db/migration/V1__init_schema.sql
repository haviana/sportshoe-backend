-- Initial schema for the SportShoe modular monolith.
-- Generic enough to support future product categories (see DOMAIN_MODEL.md).

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE categories (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code          VARCHAR(64) NOT NULL UNIQUE,
    name          VARCHAR(128) NOT NULL,
    description   TEXT,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE products (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_id   UUID NOT NULL REFERENCES categories (id),
    name          VARCHAR(256) NOT NULL,
    brand         VARCHAR(128) NOT NULL,
    attributes    JSONB NOT NULL DEFAULT '{}'::jsonb,
    images        JSONB NOT NULL DEFAULT '[]'::jsonb,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_products_category_id ON products (category_id);

CREATE TABLE offers (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id        UUID NOT NULL REFERENCES products (id),
    retailer          VARCHAR(128) NOT NULL,
    url               VARCHAR(1024) NOT NULL,
    price             NUMERIC(10, 2) NOT NULL,
    currency          VARCHAR(3) NOT NULL,
    in_stock          BOOLEAN NOT NULL DEFAULT true,
    last_checked_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_offers_product_id ON offers (product_id);

CREATE TABLE user_profiles (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id          VARCHAR(128) NOT NULL,
    category_code       VARCHAR(64) NOT NULL,
    activity_code       VARCHAR(64),
    attributes          JSONB NOT NULL DEFAULT '{}'::jsonb,
    free_text           TEXT,
    extracted_signals   JSONB NOT NULL DEFAULT '[]'::jsonb,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_user_profiles_session_id ON user_profiles (session_id);
