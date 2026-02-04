CREATE TABLE transaction_items (
    id UUID PRIMARY KEY NOT NULL,
    transaction_id UUID NOT NULL,
    product_id VARCHAR(55) NOT NULL,
    price BIGINT DEFAULT 0 NOT NULL,
    quantity INTEGER DEFAULT 0 NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL,

    CONSTRAINT fk_transaction_product FOREIGN KEY (transaction_id)
        REFERENCES transactions(id),

    CONSTRAINT unique_product_per_transaction UNIQUE (transaction_id, product_id)
);