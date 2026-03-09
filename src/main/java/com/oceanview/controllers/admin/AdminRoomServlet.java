package com.oceanview.controllers.admin;

import com.oceanview.models.Room;
import com.oceanview.services.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * GET /admin/rooms - list all rooms. POST - add room.
 * GET /admin/rooms?action=add - show add form.
 * GET /admin/rooms?action=edit&id= - show edit form.
 * POST /admin/rooms with action=edit - update room.
 * GET /admin/rooms?action=delete&id= - delete room.
 */
@WebServlet("/admin/rooms")
public class AdminRoomServlet extends HttpServlet {

    private final RoomService roomService = new RoomService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            request.getRequestDispatcher("/admin/add-room.jsp").forward(request, response);
            return;
        }
        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    roomService.getRoomById(id).ifPresent(r -> request.setAttribute("room", r));
                } catch (NumberFormatException ignored) {}
            }
            request.getRequestDispatcher("/admin/edit-room.jsp").forward(request, response);
            return;
        }
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    roomService.deleteRoom(Integer.parseInt(idStr));
                } catch (NumberFormatException ignored) {}
            }
            response.sendRedirect(request.getContextPath() + "/admin/rooms");
            return;
        }
        List<Room> rooms = roomService.getAllRoomsForAdmin();
        request.setAttribute("rooms", rooms);
        request.getRequestDispatcher("/admin/rooms.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            Room room = new Room();
            room.setRoomName(request.getParameter("roomName"));
            room.setRoomType(request.getParameter("roomType"));
            room.setPricePerNight(parseDouble(request.getParameter("pricePerNight"), 0));
            room.setDescription(request.getParameter("description"));
            room.setHasSeaView("on".equals(request.getParameter("hasSeaView")) || "1".equals(request.getParameter("hasSeaView")));
            room.setHasWifi("on".equals(request.getParameter("hasWifi")) || "1".equals(request.getParameter("hasWifi")));
            room.setHasAc("on".equals(request.getParameter("hasAc")) || "1".equals(request.getParameter("hasAc")));
            room.setHasPoolAccess("on".equals(request.getParameter("hasPoolAccess")) || "1".equals(request.getParameter("hasPoolAccess")));
            room.setBreakfastIncluded("on".equals(request.getParameter("breakfastIncluded")) || "1".equals(request.getParameter("breakfastIncluded")));
            room.setImagePath(request.getParameter("imagePath"));
            room.setAvailable(true);
            roomService.addRoom(room);
        } else if ("edit".equals(action)) {
            String idStr = request.getParameter("roomId");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    Room room = roomService.getRoomById(id).orElse(null);
                    if (room != null) {
                        room.setRoomName(request.getParameter("roomName"));
                        room.setRoomType(request.getParameter("roomType"));
                        room.setPricePerNight(parseDouble(request.getParameter("pricePerNight"), room.getPricePerNight()));
                        room.setDescription(request.getParameter("description"));
                        room.setHasSeaView("on".equals(request.getParameter("hasSeaView")) || "1".equals(request.getParameter("hasSeaView")));
                        room.setHasWifi("on".equals(request.getParameter("hasWifi")) || "1".equals(request.getParameter("hasWifi")));
                        room.setHasAc("on".equals(request.getParameter("hasAc")) || "1".equals(request.getParameter("hasAc")));
                        room.setHasPoolAccess("on".equals(request.getParameter("hasPoolAccess")) || "1".equals(request.getParameter("hasPoolAccess")));
                        room.setBreakfastIncluded("on".equals(request.getParameter("breakfastIncluded")) || "1".equals(request.getParameter("breakfastIncluded")));
                        room.setImagePath(request.getParameter("imagePath"));
                        room.setAvailable("on".equals(request.getParameter("available")) || "1".equals(request.getParameter("available")));
                        roomService.updateRoom(room);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/rooms");
    }

    private static double parseDouble(String s, double def) {
        if (s == null || s.trim().isEmpty()) return def;
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
