package com.oceanview.controllers.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * GET /admin/dashboard - admin home with stats.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("adminNav", "dashboard");
        request.setAttribute("pageTitle", "Admin Dashboard");
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
