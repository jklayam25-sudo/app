CREATE TABLE memo_views (
    id VARCHAR(100) PRIMARY KEY,
    memo_id BIGINT NOT NULL,
    employee_id UUID NOT NULL, 
 
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL,

    CONSTRAINT uk_memo_employee UNIQUE (memo_id, employee_id),
 
    CONSTRAINT fk_memo FOREIGN KEY (memo_id) REFERENCES memos(id) ON DELETE CASCADE,
    CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);
 
CREATE INDEX idx_memo_views_employee ON memo_views(employee_id);