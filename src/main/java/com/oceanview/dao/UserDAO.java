package com.oceanview.dao;

import com.oceanview.models.User;
import com.oceanview.utils.DBConnection;

import java.sql.*;
import java.util.Optional;

/**
 * Data access for users. Registration uses the same schema as H2 and MySQL.
 */
public class UserDAO {

    private static final String INSERT_USER = "INSERT INTO users (name, email, phone, password, role) VALUES (?, ?, ?, ?, 'USER')";

    public Optional<User> findByEmail(String email) {
        if (email == null || email.isEmpty()) return Optional.empty();
        String sql = "SELECT user_id, name, email, phone, password, COALESCE(role, 'USER') AS role FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> login(String emailOrUsername, String password) {
        Optional<User> opt = findByEmail(emailOrUsername);
        if (opt.isEmpty()) return Optional.empty();
        User u = opt.get();
        if (password == null || !password.equals(u.getPassword())) return Optional.empty();
        u.setPassword(null);
        return Optional.of(u);
    }

    /** Inserts a new user. Returns true if successful. */
    public boolean register(User user) {
        if (user == null) return false;
        String phone = user.getPhone() != null ? user.getPhone() : "";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, phone);
            ps.setString(4, user.getPassword());
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) user.setUserId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("role") || msg.contains("Unknown column")) {
                return registerWithoutRole(user, phone);
            }
            System.err.println("[OceanView] Register failed: " + msg);
        }
        return false;
    }

    /** Fallback for DB without role column (e.g. old MySQL schema). */
    private boolean registerWithoutRole(User user, String phone) {
        String sql = "INSERT INTO users (name, email, phone, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, phone);
            ps.setString(4, user.getPassword());
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) user.setUserId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[OceanView] Register (no role) failed: " + e.getMessage());
        }
        return false;
    }

    public Optional<User> findById(int userId) {
        String sql = "SELECT user_id, name, email, phone, password, COALESCE(role, 'USER') AS role FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setPassword(rs.getString("password"));
        try {
            u.setRole(rs.getString("role"));
        } catch (SQLException ignored) { }
        return u;
    }
}
