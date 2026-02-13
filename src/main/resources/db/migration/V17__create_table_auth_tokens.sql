CREATE TABLE auth_tokens (
    id UUID PRIMARY KEY, 
    employee_id UUID NOT NULL,
    refresh_token VARCHAR(255) NOT NULL UNIQUE,
    expired_at TIMESTAMP NOT NULL,  
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL,

    CONSTRAINT fk_employees_auth_tokens FOREIGN KEY (employee_id)
        REFERENCES employees(id)
);