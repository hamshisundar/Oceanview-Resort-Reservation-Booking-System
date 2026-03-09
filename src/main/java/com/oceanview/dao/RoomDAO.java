package com.oceanview.dao;

import com.oceanview.models.Room;
import com.oceanview.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data access for rooms table. Supports filters and availability check.
 */
public class RoomDAO {

    public List<Room> getAllRooms() {
        return findRooms(null, null, null, null);
    }

    /** All rooms including unavailable (for admin). */
    public List<Room> findAllRooms() {
        String sql = "SELECT room_id, room_name, room_type, price_per_night, description, " +
            "has_sea_view, has_wifi, has_ac, has_pool_access, breakfast_included, image_path, is_available " +
            "FROM rooms ORDER BY room_id";
        List<Room> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Room> findRooms(String roomType, Double minPrice, Double maxPrice, Boolean seaView) {
        StringBuilder sql = new StringBuilder(
            "SELECT room_id, room_name, room_type, price_per_night, description, " +
            "has_sea_view, has_wifi, has_ac, has_pool_access, breakfast_included, image_path, is_available " +
            "FROM rooms WHERE is_available = 1");
        List<Object> params = new ArrayList<>();
        if (roomType != null && !roomType.trim().isEmpty()) {
            sql.append(" AND room_type = ?");
            params.add(roomType.trim());
        }
        if (minPrice != null && minPrice > 0) {
            sql.append(" AND price_per_night >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null && maxPrice > 0) {
            sql.append(" AND price_per_night <= ?");
            params.add(maxPrice);
        }
        if (seaView != null) {
            sql.append(" AND has_sea_view = ?");
            params.add(seaView ? 1 : 0);
        }
        sql.append(" ORDER BY price_per_night");

        List<Room> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++)
                ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<Room> findById(int roomId) {
        String sql = "SELECT room_id, room_name, room_type, price_per_night, description, " +
            "has_sea_view, has_wifi, has_ac, has_pool_access, breakfast_included, image_path, is_available " +
            "FROM rooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(Room room) {
        String sql = "INSERT INTO rooms (room_name, room_type, price_per_night, description, " +
            "has_sea_view, has_wifi, has_ac, has_pool_access, breakfast_included, image_path, is_available) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setRoomParams(ps, room);
            ps.setInt(11, room.isAvailable() ? 1 : 0);
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) room.setRoomId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Room room) {
        String sql = "UPDATE rooms SET room_name=?, room_type=?, price_per_night=?, description=?, " +
            "has_sea_view=?, has_wifi=?, has_ac=?, has_pool_access=?, breakfast_included=?, image_path=?, is_available=? " +
            "WHERE room_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setRoomParams(ps, room);
            ps.setInt(11, room.isAvailable() ? 1 : 0);
            ps.setInt(12, room.getRoomId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void setRoomParams(PreparedStatement ps, Room room) throws SQLException {
        ps.setString(1, room.getRoomName());
        ps.setString(2, room.getRoomType());
        ps.setDouble(3, room.getPricePerNight());
        ps.setString(4, room.getDescription());
        ps.setInt(5, room.isHasSeaView() ? 1 : 0);
        ps.setInt(6, room.isHasWifi() ? 1 : 0);
        ps.setInt(7, room.isHasAc() ? 1 : 0);
        ps.setInt(8, room.isHasPoolAccess() ? 1 : 0);
        ps.setInt(9, room.isBreakfastIncluded() ? 1 : 0);
        ps.setString(10, room.getImagePath());
    }

    private static Room mapRow(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setRoomId(rs.getInt("room_id"));
        r.setRoomName(rs.getString("room_name"));
        r.setRoomType(rs.getString("room_type"));
        r.setPricePerNight(rs.getDouble("price_per_night"));
        r.setDescription(rs.getString("description"));
        r.setHasSeaView(rs.getInt("has_sea_view") == 1);
        r.setHasWifi(rs.getInt("has_wifi") == 1);
        r.setHasAc(rs.getInt("has_ac") == 1);
        r.setHasPoolAccess(rs.getInt("has_pool_access") == 1);
        r.setBreakfastIncluded(rs.getInt("breakfast_included") == 1);
        String imgPath = rs.getString("image_path");
        if (imgPath == null || imgPath.isEmpty()) {
            try { imgPath = rs.getString("IMAGE_PATH"); } catch (Exception ignored) { }
        }
        r.setImagePath(imgPath != null && !imgPath.isEmpty() ? imgPath : null);
        r.setAvailable(rs.getInt("is_available") == 1);
        return r;
    }
}
