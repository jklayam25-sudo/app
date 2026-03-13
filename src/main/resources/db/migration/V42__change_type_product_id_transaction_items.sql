ALTER TABLE transaction_items
    ALTER COLUMN product_id TYPE bigint USING product_id::bigint;