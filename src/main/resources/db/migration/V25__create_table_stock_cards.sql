CREATE TABLE stock_cards(

    id UUID PRIMARY KEY,
    reference_id UUID NOT NULL,
    product_id BIGINT NOT NULL, 
    type stock_move NOT NULL,
    quantity BIGINT NOT NULL,
    old_stock BIGINT NOT NULL,
    new_stock BIGINT NOT NULL,
    base_price BIGINT NOT NULL,
    description TEXT,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL,

    CONSTRAINT fk_stock_cards_products FOREIGN KEY (product_id)
        REFERENCES products(id)
);

CREATE INDEX idx_stock_cards_product ON stock_cards(product_id, created_at, id);
CREATE INDEX idx_stock_cards_global ON stock_cards(created_at, id);