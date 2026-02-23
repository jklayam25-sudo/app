CREATE TABLE customers (
    id UUID PRIMARY KEY, 
    name VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255),
    contact VARCHAR(255) NOT NULL,
    shipping_address TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE NOT NULL, 
    total_transaction BIGINT DEFAULT 0 NOT NULL,
    total_unpaid BIGINT DEFAULT 0 NOT NULL,
    total_paid BIGINT DEFAULT 0 NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL
);