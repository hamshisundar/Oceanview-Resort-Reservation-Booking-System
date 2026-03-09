package com.oceanview.dao;

import com.oceanview.models.Booking;
import com.oceanview.models.Room;
import com.oceanview.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data access for bookings table. Creates bookings and checks room availability.
 */
public class BookingDAO {

    /**
     * Check if room has any overlapping booking (exclude cancelled).
     * Overlap: existing (e_in, e_out) and new (checkIn, checkOut) overlap iff e_in < checkOut AND e_out > checkIn.
     */
    public boolean hasConflict(int roomId, String checkIn, String checkOut, Integer excludeBookingId) {
        String sql = excludeBookingId == null
            ? "SELECT 1 FROM bookings WHERE room_id = ? AND status != 'CANCELLED' AND check_in < ? AND check_out > ?"
            : "SELECT 1 FROM bookings WHERE room_id = ? AND booking_id != ? AND status != 'CANCELLED' AND check_in < ? AND check_out > ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            ps.setInt(idx++, roomId);
            if (excludeBookingId != null) ps.setInt(idx++, excludeBookingId);
            ps.setString(idx++, checkOut);
            ps.setString(idx++, checkIn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Optional<Booking> create(Booking booking) {
        if (hasConflict(booking.getRoomId(), booking.getCheckIn(), booking.getCheckOut(), null))
            return Optional.empty();
        String sql = "INSERT INTO bookings (reservation_number, user_id, room_id, check_in, check_out, total_price, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, booking.getReservationNumber());
            ps.setInt(2, booking.getUserId());
            ps.setInt(3, booking.getRoomId());
            ps.setDate(4, Date.valueOf(booking.getCheckIn()));
            ps.setDate(5, Date.valueOf(booking.getCheckOut()));
            ps.setDouble(6, booking.getTotalPrice());
            ps.setString(7, booking.getStatus() != null ? booking.getStatus() : "CONFIRMED");
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) booking.setBookingId(rs.getInt(1));
                }
                return Optional.of(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Booking> findByUserId(int userId) {
        String sql = "SELECT booking_id, reservation_number, user_id, room_id, check_in, check_out, total_price, status " +
            "FROM bookings WHERE user_id = ? ORDER BY check_in DESC";
        List<Booking> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<Booking> findByReservationNumber(String reservationNumber) {
        String sql = "SELECT booking_id, reservation_number, user_id, room_id, check_in, check_out, total_price, status " +
            "FROM bookings WHERE reservation_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reservationNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Booking> findById(int bookingId) {
        String sql = "SELECT booking_id, reservation_number, user_id, room_id, check_in, check_out, total_price, status " +
            "FROM bookings WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Booking> findAll() {
        String sql = "SELECT booking_id, reservation_number, user_id, room_id, check_in, check_out, total_price, status " +
            "FROM bookings ORDER BY created_at DESC";
        List<Booking> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Total number of bookings. */
    public long countBookings() {
        String sql = "SELECT COUNT(*) FROM bookings";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Revenue (sum total_price) for CONFIRMED/COMPLETED bookings this month. */
    public double revenueThisMonth() {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM bookings WHERE status IN ('CONFIRMED', 'COMPLETED') AND MONTH(check_in) = MONTH(CURRENT_DATE()) AND YEAR(check_in) = YEAR(CURRENT_DATE())";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Room ID with most bookings (for reports). */
    public Integer mostBookedRoomId() {
        String sql = "SELECT room_id FROM bookings WHERE status != 'CANCELLED' GROUP BY room_id ORDER BY COUNT(*) DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setReservationNumber(rs.getString("reservation_number"));
        b.setUserId(rs.getInt("user_id"));
        b.setRoomId(rs.getInt("room_id"));
        Date ci = rs.getDate("check_in");
        Date co = rs.getDate("check_out");
        b.setCheckIn(ci != null ? ci.toString() : null);
        b.setCheckOut(co != null ? co.toString() : null);
        b.setTotalPrice(rs.getDouble("total_price"));
        b.setStatus(rs.getString("status"));
        return b;
    }
}
