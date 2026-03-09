package com.oceanview.controllers.admin;

import com.oceanview.models.Booking;
import com.oceanview.services.BookingService;
import com.oceanview.services.RoomService;
import com.oceanview.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GET /admin/bookings - list all bookings.
 * POST /admin/bookings - update status (action=updateStatus, bookingId, status).
 */
@WebServlet("/admin/bookings")
public class AdminBookingServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private final RoomService roomService = new RoomService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Booking> bookings = bookingService.getAllBookings();
        Map<Integer, String> roomNames = new HashMap<>();
        Map<Integer, String> userNames = new HashMap<>();
        for (Booking b : bookings) {
            roomService.getRoomById(b.getRoomId()).ifPresent(r -> roomNames.put(r.getRoomId(), r.getRoomName()));
            userService.findById(b.getUserId()).ifPresent(u -> userNames.put(u.getUserId(), u.getName()));
        }
        request.setAttribute("bookings", bookings);
        request.setAttribute("roomNames", roomNames);
        request.setAttribute("userNames", userNames);
        request.getRequestDispatcher("/admin/bookings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("updateStatus".equals(action)) {
            String idStr = request.getParameter("bookingId");
            String status = request.getParameter("status");
            if (idStr != null && status != null && !idStr.isEmpty() && !status.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    if ("CONFIRMED".equals(status) || "CANCELLED".equals(status) || "COMPLETED".equals(status) || "PENDING".equals(status)) {
                        bookingService.updateBookingStatus(id, status);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/bookings");
    }
}
