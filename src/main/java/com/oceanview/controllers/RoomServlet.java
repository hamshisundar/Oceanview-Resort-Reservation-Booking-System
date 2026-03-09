package com.oceanview.controllers;

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
 * GET /rooms - list rooms with optional filters. Forwards to rooms.jsp.
 */
@WebServlet("/rooms")
public class RoomServlet extends HttpServlet {

    private final RoomService roomService = new RoomService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");
        String seaViewStr = request.getParameter("seaView");

        Double minPrice = parseDouble(minPriceStr);
        Double maxPrice = parseDouble(maxPriceStr);
        Boolean seaView = null;
        if (seaViewStr != null && !seaViewStr.isEmpty()) {
            seaView = "1".equals(seaViewStr) || "true".equalsIgnoreCase(seaViewStr);
        }

        // When no real filters are applied (or default "all"), use getAllRooms() so rooms display
        boolean noFilters = (type == null || type.trim().isEmpty())
            && seaView == null
            && (minPrice == null || minPrice <= 0)
            && (maxPrice == null || maxPrice >= 100000);
        List<Room> rooms = noFilters
            ? roomService.getAllRooms()
            : roomService.findRooms(type, minPrice, maxPrice, seaView);
        request.setAttribute("rooms", rooms);
        request.getRequestDispatcher("/rooms.jsp").forward(request, response);
    }

    private static Double parseDouble(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
