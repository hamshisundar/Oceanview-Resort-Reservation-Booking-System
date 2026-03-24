package com.oceanview.dao;

import com.oceanview.dto.BookingAdminRow;
import com.oceanview.models.Booking;
import com.oceanview.utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
            "FROM bookings ORDER BY booking_id DESC";
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

    /** Revenue (sum total_price) for CONFIRMED/COMPLETED bookings with check-in in the current calendar month (portable SQL). */
    public double revenueThisMonth() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = start.plusMonths(1);
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM bookings WHERE status IN ('CONFIRMED', 'COMPLETED') " +
            "AND check_in >= ? AND check_in < ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Lifetime revenue from confirmed and completed stays. */
    public double revenueAllTime() {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM bookings WHERE status IN ('CONFIRMED', 'COMPLETED')";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Guests currently in-house: confirmed or pending bookings where today is within [check_in, check_out).
     */
    public long countActiveGuests(LocalDate today) {
        if (today == null) today = LocalDate.now();
        String sql = "SELECT COUNT(DISTINCT user_id) FROM bookings WHERE status IN ('CONFIRMED', 'PENDING') " +
            "AND check_in <= ? AND check_out > ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            Date d = Date.valueOf(today);
            ps.setDate(1, d);
            ps.setDate(2, d);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<String, Long> countBookingsGroupedByStatus() {
        String sql = "SELECT status, COUNT(*) AS c FROM bookings GROUP BY status";
        Map<String, Long> map = new LinkedHashMap<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String stName = rs.getString("status");
                map.put(stName != null ? stName : "UNKNOWN", rs.getLong("c"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Admin booking list with guest and room names. sort: check_in | check_out | total_price | created_at | booking_id
     */
    public List<BookingAdminRow> findAllForAdmin(String statusFilter, String sort, String direction) {
        String sortCol = sortColumn(sort);
        String dir = "ASC".equalsIgnoreCase(direction) ? "ASC" : "DESC";
        StringBuilder sql = new StringBuilder(
            "SELECT b.booking_id, b.reservation_number, b.user_id, b.room_id, b.check_in, b.check_out, b.total_price, b.status, " +
                "u.name AS guest_name, u.email AS guest_email, r.room_name AS room_name " +
                "FROM bookings b INNER JOIN users u ON b.user_id = u.user_id INNER JOIN rooms r ON b.room_id = r.room_id ");
        List<Object> params = new ArrayList<>();
        if (statusFilter != null && !statusFilter.isBlank() && !"ALL".equalsIgnoreCase(statusFilter)) {
            sql.append("WHERE b.status = ? ");
            params.add(statusFilter.trim().toUpperCase());
        }
        sql.append("ORDER BY ").append(sortCol).append(" ").append(dir);

        List<BookingAdminRow> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapAdminRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String sortColumn(String sort) {
        if (sort == null) return "b.check_in";
        switch (sort.trim().toLowerCase()) {
            case "check_out":
                return "b.check_out";
            case "total_price":
                return "b.total_price";
            case "created_at":
                return "b.created_at";
            case "booking_id":
                return "b.booking_id";
            default:
                return "b.check_in";
        }
    }

    private static BookingAdminRow mapAdminRow(ResultSet rs) throws SQLException {
        BookingAdminRow row = new BookingAdminRow();
        row.setBookingId(rs.getInt("booking_id"));
        row.setReservationNumber(rs.getString("reservation_number"));
        row.setUserId(rs.getInt("user_id"));
        row.setRoomId(rs.getInt("room_id"));
        Date ci = rs.getDate("check_in");
        Date co = rs.getDate("check_out");
        row.setCheckIn(ci != null ? ci.toString() : null);
        row.setCheckOut(co != null ? co.toString() : null);
        row.setTotalPrice(rs.getDouble("total_price"));
        row.setStatus(rs.getString("status"));
        row.setGuestName(rs.getString("guest_name"));
        row.setGuestEmail(rs.getString("guest_email"));
        row.setRoomName(rs.getString("room_name"));
        return row;
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
