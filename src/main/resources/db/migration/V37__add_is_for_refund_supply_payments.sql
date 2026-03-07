ALTER TABLE supply_payments
    ADD COLUMN  is_for_refund BOOLEAN DEFAULT false;