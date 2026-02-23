ALTER TABLE transactions
    ADD COLUMN customer_id UUID NOT NULL;

ALTER TABLE transactions
    ADD CONSTRAINT fk_customers_transactions FOREIGN KEY (customer_id)
        REFERENCES customers(id);