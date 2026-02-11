ALTER TABLE transactions ADD COLUMN total_unrefunded BIGINT DEFAULT 0;
ALTER TABLE transactions ADD COLUMN total_refunded BIGINT DEFAULT 0;