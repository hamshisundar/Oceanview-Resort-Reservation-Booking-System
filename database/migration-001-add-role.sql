-- Run this only if your users table was created without the role column (existing databases).
-- If you get "Duplicate column name 'role'", the column already exists; skip the ALTER.
USE oceanview_db;

ALTER TABLE users ADD COLUMN role VARCHAR(20) DEFAULT 'USER' COMMENT 'USER or ADMIN';

-- Ensure admin user exists (ignore if already present)
INSERT IGNORE INTO users (name, email, phone, password, role)
VALUES ('Admin', 'admin@oceanview.com', '0000000000', 'admin123', 'ADMIN');
