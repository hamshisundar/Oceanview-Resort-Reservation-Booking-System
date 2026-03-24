package com.oceanview.controllers.api;

import com.oceanview.api.ApiJson;
import com.oceanview.dto.BookingAdminRow;
import com.oceanview.dto.BookingStatusPayload;
import com.oceanview.services.BookingService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Admin REST: GET /api/bookings, PUT /api/bookings/{id}/status
 */
@WebServlet(urlPatterns = {"/api/bookings", "/api/bookings/*"})
public class ApiBookingsServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ApiJson.requireAdmin(req) == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        String status = req.getParameter("status");
        String sort = req.getParameter("sort");
        String dir = req.getParameter("dir");
        List<Map<String, Object>> list = bookingService.getAdminBookings(status, sort, dir).stream()
                .map(this::toJson)
                .collect(Collectors.toList());
        ApiJson.writeJson(resp, HttpServletResponse.SC_OK, list);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ApiJson.requireAdmin(req) == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        String pi = req.getPathInfo();
        if (pi == null || !pi.startsWith("/")) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
            return;
        }
        String[] segs = pi.substring(1).split("/");
        if (segs.length != 2 || !"status".equals(segs[1])) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Use PUT /api/bookings/{id}/status");
            return;
        }
        int bookingId;
        try {
            bookingId = Integer.parseInt(segs[0]);
        } catch (NumberFormatException e) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid booking id");
            return;
        }
        BookingStatusPayload body = ApiJson.readJson(req, BookingStatusPayload.class);
        if (body == null || body.getStatus() == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "status required");
            return;
        }
        String st = body.getStatus().trim().toUpperCase();
        if (!isAllowedStatus(st)) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid status");
            return;
        }
        if (bookingService.getById(bookingId).isEmpty()) {
            ApiJson.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Booking not found");
            return;
        }
        if (bookingService.updateBookingStatus(bookingId, st)) {
            ApiJson.writeJson(resp, HttpServletResponse.SC_OK, Map.of("id", bookingId, "status", st));
        } else {
            ApiJson.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Update failed");
        }
    }

    private static boolean isAllowedStatus(String s) {
        return "CONFIRMED".equals(s) || "PENDING".equals(s) || "CANCELLED".equals(s) || "COMPLETED".equals(s);
    }

    private Map<String, Object> toJson(BookingAdminRow b) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", b.getBookingId());
        m.put("reservationNumber", b.getReservationNumber());
        m.put("userId", b.getUserId());
        m.put("guestName", b.getGuestName());
        m.put("guestEmail", b.getGuestEmail());
        m.put("roomId", b.getRoomId());
        m.put("roomName", b.getRoomName());
        m.put("checkIn", b.getCheckIn());
        m.put("checkOut", b.getCheckOut());
        m.put("totalPrice", b.getTotalPrice());
        m.put("status", b.getStatus());
        return m;
    }
}
