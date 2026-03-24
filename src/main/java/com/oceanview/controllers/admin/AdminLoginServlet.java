package com.oceanview.controllers.admin;

import com.oceanview.models.User;
import com.oceanview.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Admin-only login at /admin/login. Guests should use /login.jsp.
 */
@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String source = request.getParameter("source");
        Optional<User> user = userService.login(email, password);
        if (user.isPresent() && "ADMIN".equals(user.get().getRole())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user.get());
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }
        if ("main-login".equals(source)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?role=admin&adminError=1");
            return;
        }
        request.setAttribute("error", "Invalid credentials or not an administrator.");
        request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
    }
}
