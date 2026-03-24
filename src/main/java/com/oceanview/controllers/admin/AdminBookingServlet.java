package com.oceanview.controllers.admin;

import com.oceanview.services.BookingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * GET /admin/bookings - list all bookings.
 * POST /admin/bookings - update status (action=updateStatus, bookingId, status).
 */
@WebServlet("/admin/bookings")
public class AdminBookingServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("adminNav", "bookings");
        request.setAttribute("pageTitle", "Booking Management");
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
