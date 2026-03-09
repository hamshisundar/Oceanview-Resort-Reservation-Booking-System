package com.oceanview.services;

import com.oceanview.dao.RoomDAO;
import com.oceanview.models.Room;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for room listing and retrieval.
 */
public class RoomService {

    private final RoomDAO roomDAO = new RoomDAO();

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    /** All rooms including unavailable (for admin). */
    public List<Room> getAllRoomsForAdmin() {
        return roomDAO.findAllRooms();
    }

    public List<Room> findRooms(String roomType, Double minPrice, Double maxPrice, Boolean seaView) {
        return roomDAO.findRooms(roomType, minPrice, maxPrice, seaView);
    }

    public Optional<Room> getRoomById(int roomId) {
        return roomDAO.findById(roomId);
    }

    public boolean addRoom(Room room) {
        if (room == null || room.getRoomName() == null || room.getRoomType() == null) return false;
        return roomDAO.insert(room);
    }

    public boolean updateRoom(Room room) {
        if (room == null || room.getRoomId() <= 0) return false;
        return roomDAO.update(room);
    }

    public boolean deleteRoom(int roomId) {
        return roomDAO.delete(roomId);
    }
}
