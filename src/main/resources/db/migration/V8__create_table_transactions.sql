CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    invoice_id VARCHAR(55) UNIQUE NOT NULL,
    total_items INTEGER DEFAULT 0 NOT NULL,
    total_fee BIGINT DEFAULT 0 NOT NULL,
    total_discount BIGINT DEFAULT 0 NOT NULL,
    sub_total BIGINT DEFAULT 0 NOT NULL,
    grand_total BIGINT DEFAULT 0 NOT NULL,
    total_unpaid BIGINT DEFAULT 0 NOT NULL,
    total_paid BIGINT DEFAULT 0 NOT NULL,
    status transaction_status DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL
);