ALTER TABLE supplies
    ADD COLUMN supplier_id UUID NOT NULL;

ALTER TABLE supplies
    ADD CONSTRAINT fk_suppliers_supplies FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id);