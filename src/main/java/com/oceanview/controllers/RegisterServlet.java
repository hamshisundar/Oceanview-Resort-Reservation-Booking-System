package com.oceanview.controllers;

import com.oceanview.models.User;
import com.oceanview.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles POST /register. Uses built-in H2 by default so registration works without MySQL.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String ctx = request.getContextPath();
        if (ctx == null) ctx = "";

        try {
            String name = trim(request.getParameter("name"));
            String email = trim(request.getParameter("email"));
            String phone = trim(request.getParameter("phone"));
            String password = request.getParameter("password");

            if (name.isEmpty() || email.isEmpty() || password == null || password.isEmpty()) {
                response.sendRedirect(ctx + "/register.jsp?error=validation");
                return;
            }
            if (userService.findByEmail(email).isPresent()) {
                response.sendRedirect(ctx + "/register.jsp?error=email");
                return;
            }

            User user = new User(name, email, phone, password);
            if (userService.register(user)) {
                response.sendRedirect(ctx + "/login.jsp?registered=1");
            } else {
                response.sendRedirect(ctx + "/register.jsp?error=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(ctx + "/register.jsp?error=1");
        }
    }

    private static String trim(String s) {
        return s != null ? s.trim() : "";
    }
}
