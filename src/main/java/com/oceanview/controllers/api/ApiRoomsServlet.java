package com.oceanview.controllers.api;

import com.oceanview.api.ApiJson;
import com.oceanview.dto.RoomWritePayload;
import com.oceanview.models.Room;
import com.oceanview.services.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Part;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Admin REST: rooms CRUD. Maps to spec paths under context root: GET/POST /api/rooms, PUT/DELETE /api/rooms/{id}
 */
@WebServlet(urlPatterns = {"/api/rooms", "/api/rooms/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class ApiRoomsServlet extends HttpServlet {

    private final RoomService roomService = new RoomService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ApiJson.requireAdmin(req) == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        List<Map<String, Object>> list = roomService.getAllRoomsForAdmin().stream()
                .map(this::toJson)
                .collect(Collectors.toList());
        ApiJson.writeJson(resp, HttpServletResponse.SC_OK, list);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (ApiJson.requireAdmin(req) == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        String pathInfo = req.getPathInfo();
        if ("/upload".equals(pathInfo)) {
            handleImageUpload(req, resp);
            return;
        }
        RoomWritePayload payload = ApiJson.readJson(req, RoomWritePayload.class);
        Room room = new Room();
        applyPayload(room, payload, true);
        if (room.getRoomName() == null || room.getRoomName().isBlank()
                || room.getRoomType() == null || room.getRoomType().isBlank()) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "name and type are required");
            return;
        }
        if (roomService.addRoom(room)) {
            ApiJson.writeJson(resp, HttpServletResponse.SC_CREATED, toJson(room));
        } else {
            ApiJson.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create room");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ApiJson.requireAdmin(req) == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        Integer id = pathId(req.getPathInfo());
        if (id == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing room id");
            return;
        }
        Room existing = roomService.getRoomById(id).orElse(null);
        if (existing == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Room not found");
            return;
        }
        RoomWritePayload payload = ApiJson.readJson(req, RoomWritePayload.class);
        applyPayload(existing, payload, false);
        if (roomService.updateRoom(existing)) {
            ApiJson.writeJson(resp, HttpServletResponse.SC_OK, toJson(existing));
        } else {
            ApiJson.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not update room");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ApiJson.requireAdmin(req) == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        Integer id = pathId(req.getPathInfo());
        if (id == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing room id");
            return;
        }
        if (roomService.getRoomById(id).isEmpty()) {
            ApiJson.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Room not found");
            return;
        }
        if (roomService.deleteRoom(id)) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            ApiJson.writeError(resp, HttpServletResponse.SC_CONFLICT,
                    "Cannot delete room: it may have existing bookings.");
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

    private Map<String, Object> toJson(Room r) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", r.getRoomId());
        m.put("name", r.getRoomName());
        m.put("type", r.getRoomType());
        m.put("price", r.getPricePerNight());
        m.put("available", r.isAvailable());
        m.put("description", r.getDescription());
        m.put("hasSeaView", r.isHasSeaView());
        m.put("hasWifi", r.isHasWifi());
        m.put("hasAc", r.isHasAc());
        m.put("hasPoolAccess", r.isHasPoolAccess());
        m.put("breakfastIncluded", r.isBreakfastIncluded());
        m.put("imagePath", r.getImagePath());
        return m;
    }

    private static void applyPayload(Room room, RoomWritePayload p, boolean create) {
        if (p == null) return;
        if (p.getName() != null) room.setRoomName(p.getName());
        if (p.getType() != null) room.setRoomType(p.getType());
        if (p.getPrice() != null) room.setPricePerNight(p.getPrice());
        if (p.getDescription() != null) room.setDescription(p.getDescription());
        if (p.getAvailable() != null) room.setAvailable(p.getAvailable());
        if (p.getHasSeaView() != null) room.setHasSeaView(p.getHasSeaView());
        if (p.getHasWifi() != null) room.setHasWifi(p.getHasWifi());
        if (p.getHasAc() != null) room.setHasAc(p.getHasAc());
        if (p.getHasPoolAccess() != null) room.setHasPoolAccess(p.getHasPoolAccess());
        if (p.getBreakfastIncluded() != null) room.setBreakfastIncluded(p.getBreakfastIncluded());
        if (p.getImagePath() != null) room.setImagePath(p.getImagePath());
        if (create) {
            if (p.getAvailable() == null) room.setAvailable(true);
            if (p.getHasWifi() == null) room.setHasWifi(true);
            if (p.getHasAc() == null) room.setHasAc(true);
            if (p.getDescription() == null) room.setDescription("");
            if (p.getImagePath() == null) room.setImagePath("images/rooms/standard.png");
        }
    }

    private void handleImageUpload(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String ct = req.getContentType();
        if (ct == null || !ct.toLowerCase().startsWith("multipart/")) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Expected multipart form-data");
            return;
        }
        Part part = req.getPart("image");
        if (part == null || part.getSize() == 0) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "No image selected");
            return;
        }
        String submitted = part.getSubmittedFileName();
        if (submitted == null || submitted.isBlank()) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid file name");
            return;
        }
        String ext = extensionOf(submitted);
        if (!isAllowedImageExt(ext)) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Only JPG, PNG, GIF, WEBP files are allowed");
            return;
        }

        String realWebRoot = req.getServletContext().getRealPath("/");
        if (realWebRoot == null) {
            ApiJson.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Upload path unavailable");
            return;
        }
        Path roomImgDir = Paths.get(realWebRoot, "images", "rooms");
        Files.createDirectories(roomImgDir);
        String filename = "room-" + UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Path target = roomImgDir.resolve(filename);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        ApiJson.writeJson(resp, HttpServletResponse.SC_OK, Map.of("imagePath", "images/rooms/" + filename));
    }

    private static String extensionOf(String name) {
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) return "";
        return name.substring(dot + 1).toLowerCase();
    }

    private static boolean isAllowedImageExt(String ext) {
        return "jpg".equals(ext) || "jpeg".equals(ext) || "png".equals(ext) || "gif".equals(ext) || "webp".equals(ext);
    }
}
