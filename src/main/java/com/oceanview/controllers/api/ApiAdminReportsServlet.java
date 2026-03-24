package com.oceanview.controllers.api;

import com.oceanview.api.ApiJson;
import com.oceanview.dao.BookingDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.UserDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GET /api/admin/reports — extended metrics for the reports page.
 */
@WebServlet("/api/admin/reports")
public class ApiAdminReportsServlet extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ApiJson.requireAdmin(req) == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("totalRooms", roomDAO.findAllRooms().size());
        body.put("totalBookings", bookingDAO.countBookings());
        body.put("totalUsers", userDAO.findAll().size());
        body.put("revenueThisMonth", bookingDAO.revenueThisMonth());
        body.put("revenueAllTime", bookingDAO.revenueAllTime());
        body.put("bookingsByStatus", bookingDAO.countBookingsGroupedByStatus());
        Integer topRoomId = bookingDAO.mostBookedRoomId();
        body.put("mostBookedRoomId", topRoomId);
        body.put("mostBookedRoomName", topRoomId != null
                ? roomDAO.findById(topRoomId).map(r -> r.getRoomName()).orElse("—")
                : "—");
        ApiJson.writeJson(resp, HttpServletResponse.SC_OK, body);
    }
}
