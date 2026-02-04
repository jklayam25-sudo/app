CREATE TABLE transaction_payments (
    id UUID PRIMARY KEY NOT NULL,
    transaction_id UUID NOT NULL,
    total_payment BIGINT NOT NULL,
    payment_from VARCHAR(100) NOT NULL,
    payment_to VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL,

    CONSTRAINT fk_transaction_payments FOREIGN KEY (transaction_id)
        REFERENCES transactions(id),
);