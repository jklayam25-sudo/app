CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    base_price INTEGER NOT NULL,
    sell_price INTEGER NOT NULL,
    stock_quantity INTEGER NOT NULL,
    stock_minimum INTEGER NOT NULL DEFAULT 0,
    category_id INTEGER,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_products_categories
        FOREIGN KEY(category_id) 
        REFERENCES categories(id)
); 