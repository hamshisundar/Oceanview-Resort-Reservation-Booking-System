package com.oceanview.controllers.api;

import com.oceanview.api.ApiJson;
import com.oceanview.dao.BookingDAO;
import com.oceanview.dao.RoomDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GET /api/admin/stats — dashboard KPIs from the database.
 */
@WebServlet("/api/admin/stats")
public class ApiAdminStatsServlet extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ApiJson.requireAdmin(req) == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        int totalRooms = roomDAO.findAllRooms().size();
        long totalBookings = bookingDAO.countBookings();
        long activeGuests = bookingDAO.countActiveGuests(LocalDate.now());
        double revenueThisMonth = bookingDAO.revenueThisMonth();
        double revenueAllTime = bookingDAO.revenueAllTime();
        Integer topRoomId = bookingDAO.mostBookedRoomId();
        String topRoomName = topRoomId != null
                ? roomDAO.findById(topRoomId).map(r -> r.getRoomName()).orElse("—")
                : "—";

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("totalRooms", totalRooms);
        body.put("totalBookings", totalBookings);
        body.put("activeGuests", activeGuests);
        body.put("revenueThisMonth", revenueThisMonth);
        body.put("revenueAllTime", revenueAllTime);
        body.put("mostBookedRoomName", topRoomName);
        body.put("mostBookedRoomId", topRoomId);
        ApiJson.writeJson(resp, HttpServletResponse.SC_OK, body);
    }
}
