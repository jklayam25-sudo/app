CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    base_price BIGINT NOT NULL,
    sell_price BIGINT NOT NULL,
    stock_quantity BIGINT NOT NULL,
    stock_minimum BIGINT NOT NULL DEFAULT 0,
    category_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_products_categories
        FOREIGN KEY(category_id) 
        REFERENCES categories(id)
); 