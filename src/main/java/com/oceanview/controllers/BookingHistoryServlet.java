package com.oceanview.controllers;

import com.oceanview.models.Booking;
import com.oceanview.models.User;
import com.oceanview.services.BookingService;
import com.oceanview.services.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GET /my-bookings - list bookings for logged-in user. Forwards to booking-history.jsp.
 */
@WebServlet("/my-bookings")
public class BookingHistoryServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private final RoomService roomService = new RoomService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        List<Booking> bookings = new ArrayList<>();
        Map<Integer, String> roomNames = new HashMap<>();
        if (user != null) {
            bookings = bookingService.getBookingsByUserId(user.getUserId());
            for (Booking b : bookings) {
                roomService.getRoomById(b.getRoomId()).ifPresent(r -> roomNames.put(r.getRoomId(), r.getRoomName()));
            }
        }
        request.setAttribute("bookings", bookings);
        request.setAttribute("roomNames", roomNames);
        request.getRequestDispatcher("/booking-history.jsp").forward(request, response);
    }
}
