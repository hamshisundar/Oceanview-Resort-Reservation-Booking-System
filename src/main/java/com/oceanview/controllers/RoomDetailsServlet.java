package com.oceanview.controllers;

import com.oceanview.models.Room;
import com.oceanview.services.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * GET /room-details?id=... - show single room. Forwards to room-details.jsp or 404.
 */
@WebServlet("/room-details")
public class RoomDetailsServlet extends HttpServlet {

    private final RoomService roomService = new RoomService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/rooms");
            return;
        }
        int roomId;
        try {
            roomId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/rooms");
            return;
        }
        Optional<Room> room = roomService.getRoomById(roomId);
        if (room.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/rooms");
            return;
        }
        request.setAttribute("room", room.get());
        request.getRequestDispatcher("/room-details.jsp").forward(request, response);
    }
}
