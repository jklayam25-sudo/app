ALTER TABLE supply_items
    ALTER COLUMN product_id TYPE bigint USING product_id::bigint;

ALTER TABLE supply_items
    ADD CONSTRAINT fk_supply_items_product FOREIGN KEY (product_id)
        REFERENCES products(id);