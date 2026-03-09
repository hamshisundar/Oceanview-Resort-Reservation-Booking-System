package com.oceanview.controllers;

import com.oceanview.models.Booking;
import com.oceanview.models.User;
import com.oceanview.services.BookingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * POST /book-room - create booking. Requires logged-in user. Redirects to confirmation or booking form with error.
 */
@WebServlet("/book-room")
public class BookingServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=book");
            return;
        }

        String roomIdStr = request.getParameter("roomId");
        String checkIn = request.getParameter("checkIn");
        String checkOut = request.getParameter("checkOut");
        if (roomIdStr == null || checkIn == null || checkOut == null ||
            checkIn.trim().isEmpty() || checkOut.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/rooms");
            return;
        }
        int roomId;
        try {
            roomId = Integer.parseInt(roomIdStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/rooms");
            return;
        }

        Optional<Booking> booking = bookingService.createBooking(user.getUserId(), roomId, checkIn.trim(), checkOut.trim());
        if (booking.isPresent()) {
            response.sendRedirect(request.getContextPath() + "/booking-confirmation.jsp?reservation=" +
                booking.get().getReservationNumber() + "&roomId=" + roomId + "&checkIn=" + checkIn + "&checkOut=" + checkOut +
                "&total=" + (long) Math.round(booking.get().getTotalPrice()) + "&guestName=" + java.net.URLEncoder.encode(user.getName(), "UTF-8"));
        } else {
            response.sendRedirect(request.getContextPath() + "/room-details?id=" + roomId + "&error=unavailable");
        }
    }
}
