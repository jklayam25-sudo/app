ALTER TABLE memos
    CREATE INDEX idx_active_memos_role ON memos(role, updated_at) WHERE is_active = true;