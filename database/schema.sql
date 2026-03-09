-- Ocean View Hotel Booking System - MySQL Schema
-- Run this script to create the database and tables.

CREATE DATABASE IF NOT EXISTS oceanview_db;
USE oceanview_db;

-- Users (guests and admins; role distinguishes)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'USER' COMMENT 'USER or ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Admin users
CREATE TABLE admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Room types and amenities
CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_name VARCHAR(100) NOT NULL,
    room_type VARCHAR(50) NOT NULL COMMENT 'Standard, Deluxe, Ocean View, Luxury Suite',
    price_per_night DECIMAL(10, 2) NOT NULL,
    description TEXT,
    has_sea_view TINYINT(1) DEFAULT 0,
    has_wifi TINYINT(1) DEFAULT 1,
    has_ac TINYINT(1) DEFAULT 1,
    has_pool_access TINYINT(1) DEFAULT 0,
    breakfast_included TINYINT(1) DEFAULT 0,
    image_path VARCHAR(255),
    is_available TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bookings
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_number VARCHAR(50) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED' COMMENT 'PENDING, CONFIRMED, CANCELLED, COMPLETED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE RESTRICT
);

-- Optional: Payments (for future use)
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50),
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);

-- Indexes for common queries
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_room ON bookings(room_id);
CREATE INDEX idx_bookings_dates ON bookings(check_in, check_out);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_rooms_type ON rooms(room_type);
CREATE INDEX idx_rooms_available ON rooms(is_available);

-- Insert default admin (password: admin123 - CHANGE IN PRODUCTION)
-- Using simple hash for demo; use BCrypt in production
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');

-- Admin user in users table (login with email + password, role=ADMIN)
INSERT INTO users (name, email, phone, password, role)
VALUES ('Admin', 'admin@oceanview.com', '0000000000', 'admin123', 'ADMIN');

-- Sample rooms
INSERT INTO rooms (room_name, room_type, price_per_night, description, has_sea_view, has_wifi, has_ac, has_pool_access, breakfast_included, image_path) VALUES
('Standard Double', 'Standard Room', 27500.00, 'Comfortable double room with basic amenities.', 0, 1, 1, 0, 0, 'images/rooms/standard.png'),
('Deluxe Ocean', 'Deluxe Room', 39000.00, 'Spacious room with partial sea view.', 1, 1, 1, 1, 0, 'images/rooms/deluxe.png'),
('Ocean View Premium', 'Ocean View Room', 53500.00, 'Stunning ocean view with balcony.', 1, 1, 1, 1, 1, 'images/rooms/oceanview.png'),
('Luxury Suite', 'Luxury Suite', 91000.00, 'Premium suite with full ocean view and all amenities.', 1, 1, 1, 1, 1, 'images/rooms/suite.png');
