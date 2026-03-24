package com.oceanview.api;

import com.oceanview.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * JSON helpers for admin API servlets.
 */
public final class ApiJson {

    private ApiJson() {}

    public static User requireAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        Object u = session.getAttribute("user");
        if (!(u instanceof User)) return null;
        User user = (User) u;
        if (!"ADMIN".equals(user.getRole())) return null;
        return user;
    }

    public static void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json;charset=UTF-8");
        byte[] json = JsonMapper.get().writeValueAsBytes(body);
        resp.getOutputStream().write(json);
    }

    public static void writeError(HttpServletResponse resp, int status, String message) throws IOException {
        writeJson(resp, status, Map.of("error", message != null ? message : "Error"));
    }

    public static <T> T readJson(HttpServletRequest req, Class<T> type) throws IOException {
        return JsonMapper.get().readValue(req.getInputStream(), type);
    }
}
