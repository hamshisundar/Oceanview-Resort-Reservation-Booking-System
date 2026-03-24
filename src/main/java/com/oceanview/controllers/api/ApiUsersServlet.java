package com.oceanview.controllers.api;

import com.oceanview.api.ApiJson;
import com.oceanview.dao.UserDAO;
import com.oceanview.models.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Admin REST: GET /api/users, DELETE /api/users/{id}
 */
@WebServlet(urlPatterns = {"/api/users", "/api/users/*"})
public class ApiUsersServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User admin = ApiJson.requireAdmin(req);
        if (admin == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        List<Map<String, Object>> list = userDAO.findAll().stream()
                .map(u -> toPublicJson(u, admin.getUserId()))
                .collect(Collectors.toList());
        ApiJson.writeJson(resp, HttpServletResponse.SC_OK, list);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User admin = ApiJson.requireAdmin(req);
        if (admin == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        Integer id = pathId(req.getPathInfo());
        if (id == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing user id");
            return;
        }
        if (id == admin.getUserId()) {
            ApiJson.writeError(resp, HttpServletResponse.SC_FORBIDDEN, "You cannot delete your own account");
            return;
        }
        Optional<User> target = userDAO.findById(id);
        if (target.isEmpty()) {
            ApiJson.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        if ("ADMIN".equalsIgnoreCase(target.get().getRole()) && userDAO.countAdmins() <= 1) {
            ApiJson.writeError(resp, HttpServletResponse.SC_FORBIDDEN, "Cannot delete the last administrator");
            return;
        }
        if (userDAO.deleteById(id)) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            ApiJson.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Delete failed");
        }
    }

    private static Integer pathId(String pathInfo) {
        if (pathInfo == null || pathInfo.length() < 2) return null;
        String s = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s.split("/")[0]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Map<String, Object> toPublicJson(User u, int currentAdminId) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", u.getUserId());
        m.put("name", u.getName());
        m.put("email", u.getEmail());
        m.put("phone", u.getPhone());
        m.put("role", u.getRole() != null ? u.getRole() : "USER");
        m.put("isCurrentUser", u.getUserId() == currentAdminId);
        return m;
    }
}
