ALTER TABLE stock_cards
    RENAME COLUMN base_price TO old_price;

ALTER TABLE stock_cards
    ADD COLUMN new_price BIGINT NOT NULL;
