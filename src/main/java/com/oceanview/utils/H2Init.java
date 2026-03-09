package com.oceanview.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates H2 schema and default data when using H2 fallback (no MySQL).
 */
final class H2Init {

    static void initSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(150) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "phone VARCHAR(20), " +
                "role VARCHAR(20) DEFAULT 'USER', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            st.execute("CREATE TABLE IF NOT EXISTS rooms (" +
                "room_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "room_name VARCHAR(100) NOT NULL, " +
                "room_type VARCHAR(50) NOT NULL, " +
                "price_per_night DECIMAL(10,2) NOT NULL, " +
                "description TEXT, " +
                "has_sea_view INT DEFAULT 0, " +
                "has_wifi INT DEFAULT 1, " +
                "has_ac INT DEFAULT 1, " +
                "has_pool_access INT DEFAULT 0, " +
                "breakfast_included INT DEFAULT 0, " +
                "image_path VARCHAR(255), " +
                "is_available INT DEFAULT 1, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            st.execute("CREATE TABLE IF NOT EXISTS bookings (" +
                "booking_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "reservation_number VARCHAR(50) NOT NULL UNIQUE, " +
                "user_id INT NOT NULL, " +
                "room_id INT NOT NULL, " +
                "check_in DATE NOT NULL, " +
                "check_out DATE NOT NULL, " +
                "total_price DECIMAL(10,2) NOT NULL, " +
                "status VARCHAR(20) DEFAULT 'CONFIRMED', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE, " +
                "FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE RESTRICT)");

            // Default admin and sample rooms only if empty (H2: use FROM and WHERE)
            st.execute("INSERT INTO users (name, email, phone, password, role) " +
                "SELECT 'Admin', 'admin@oceanview.com', '0000000000', 'admin123', 'ADMIN' FROM (SELECT 1) " +
                "WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@oceanview.com')");
            st.execute("INSERT INTO rooms (room_name, room_type, price_per_night, description, has_sea_view, has_wifi, has_ac, has_pool_access, breakfast_included, image_path) " +
                "SELECT 'Standard Double', 'Standard Room', 27500.00, 'Comfortable double room with basic amenities.', 0, 1, 1, 0, 0, 'images/rooms/standard.png' FROM (SELECT 1) " +
                "WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE room_name = 'Standard Double')");
            st.execute("INSERT INTO rooms (room_name, room_type, price_per_night, description, has_sea_view, has_wifi, has_ac, has_pool_access, breakfast_included, image_path) " +
                "SELECT 'Deluxe Ocean', 'Deluxe Room', 39000.00, 'Spacious room with partial sea view.', 1, 1, 1, 1, 0, 'images/rooms/deluxe.png' FROM (SELECT 1) " +
                "WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE room_name = 'Deluxe Ocean')");
            st.execute("INSERT INTO rooms (room_name, room_type, price_per_night, description, has_sea_view, has_wifi, has_ac, has_pool_access, breakfast_included, image_path) " +
                "SELECT 'Ocean View Premium', 'Ocean View Room', 53500.00, 'Stunning ocean view with balcony.', 1, 1, 1, 1, 1, 'images/rooms/oceanview.png' FROM (SELECT 1) " +
                "WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE room_name = 'Ocean View Premium')");
            st.execute("INSERT INTO rooms (room_name, room_type, price_per_night, description, has_sea_view, has_wifi, has_ac, has_pool_access, breakfast_included, image_path) " +
                "SELECT 'Luxury Suite', 'Luxury Suite', 91000.00, 'Premium suite with full ocean view and all amenities.', 1, 1, 1, 1, 1, 'images/rooms/suite.png' FROM (SELECT 1) " +
                "WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE room_name = 'Luxury Suite')");

            // Ensure image paths and LKR prices (fixes existing rows)
            st.execute("UPDATE rooms SET image_path = 'images/rooms/standard.png', price_per_night = 27500 WHERE room_name = 'Standard Double'");
            st.execute("UPDATE rooms SET image_path = 'images/rooms/deluxe.png', price_per_night = 39000 WHERE room_name = 'Deluxe Ocean'");
            st.execute("UPDATE rooms SET image_path = 'images/rooms/oceanview.png', price_per_night = 53500 WHERE room_name = 'Ocean View Premium'");
            st.execute("UPDATE rooms SET image_path = 'images/rooms/suite.png', price_per_night = 91000 WHERE room_name = 'Luxury Suite'");
        }
    }
}
