CREATE TABLE supply_items (
    id UUID PRIMARY KEY NOT NULL,
    supply_id UUID NOT NULL,
    product_id VARCHAR(55) NOT NULL,
    price BIGINT DEFAULT 0 NOT NULL,
    quantity INTEGER DEFAULT 0 NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL,

    CONSTRAINT fk_supply_items FOREIGN KEY (supply_id)
        REFERENCES supplies(id)
 
);