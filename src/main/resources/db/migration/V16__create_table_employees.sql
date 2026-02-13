CREATE TABLE employees (
    id UUID PRIMARY KEY,
    username VARCHAR(55) NOT NULL UNIQUE,
    fullname VARCHAR(55) NOT NULL,
    password VARCHAR(255) NOT NULL,
    join_date TIMESTAMP NOT NULL,
    last_ip VARCHAR(55),
    last_device VARCHAR(55),
    is_active BOOLEAN NOT NULL,
    role employee_role DEFAULT 'CASHIER',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL
);