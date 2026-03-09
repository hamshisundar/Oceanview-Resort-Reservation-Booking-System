package com.oceanview.controllers.admin;

import com.oceanview.dao.BookingDAO;
import com.oceanview.dao.RoomDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * GET /admin/reports - booking and revenue reports.
 */
@WebServlet("/admin/reports")
public class AdminReportServlet extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int totalRooms = roomDAO.findAllRooms().size();
        long totalBookings = bookingDAO.countBookings();
        double revenueThisMonth = bookingDAO.revenueThisMonth();
        Integer topRoomId = bookingDAO.mostBookedRoomId();
        String mostBookedRoomName = topRoomId != null ? roomDAO.findById(topRoomId).map(r -> r.getRoomName()).orElse("—") : "—";
        request.setAttribute("totalRooms", totalRooms);
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("revenueThisMonth", revenueThisMonth);
        request.setAttribute("mostBookedRoomName", mostBookedRoomName);
        request.getRequestDispatcher("/admin/reports.jsp").forward(request, response);
    }
}
