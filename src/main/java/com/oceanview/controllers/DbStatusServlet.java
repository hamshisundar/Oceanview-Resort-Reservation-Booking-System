package com.oceanview.controllers;

import com.oceanview.utils.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database status page. Writes HTML directly so it always returns a valid response
 * even when DB fails or JSP is missing. Open /oceanview/db-status to fix registration.
 */
@WebServlet("/db-status")
public class DbStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String message;
        String detail = null;
        boolean ok = false;

        try {
            try (Connection conn = DBConnection.getConnection()) {
                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        message = "Database connection: OK. Table 'users' exists (" + count + " user(s)).";
                        ok = true;
                    } else {
                        message = "Database connection: OK. Table 'users' could not be read.";
                        ok = true;
                    }
                }
            }
        } catch (Throwable e) {
            message = "Database connection FAILED.";
            detail = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
        }

        String ctx = request.getContextPath();
        if (ctx == null) ctx = "";

        PrintWriter out = response.getWriter();
        out.print("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\">");
        out.print("<title>Database Status - Ocean View</title>");
        out.print("<style>body{font-family:sans-serif;max-width:600px;margin:2rem auto;padding:0 1rem;} a{color:#0d5c8c;} .ok{color:#0a6b0a;} .err{background:#fff5f5;border:1px solid #e0a0a0;padding:1rem;border-radius:8px;margin:1rem 0;} code{background:#f0f0f0;padding:2px 6px;}</style>");
        out.print("</head><body>");
        out.print("<h1>Database status</h1>");
        out.print("<p><a href=\"" + ctx + "/\">Home</a> | <a href=\"" + ctx + "/register.jsp\">Register</a></p>");

        if (ok) {
            out.print("<p class=\"ok\"><strong>" + escapeHtml(message) + "</strong></p>");
            if (DBConnection.isUsingH2()) {
                out.print("<p style=\"color:#666;font-size:0.9rem;\">Using built-in H2 (MySQL was not available). Registration and booking work without any database setup.</p>");
            }
            out.print("<p><a href=\"" + ctx + "/register.jsp\">Try registration</a></p>");
        } else {
            out.print("<div class=\"err\"><p><strong>" + escapeHtml(message) + "</strong></p>");
            if (detail != null && !detail.isEmpty()) {
                out.print("<p><strong>Error:</strong> " + escapeHtml(detail) + "</p>");
            }
            out.print("</div>");
            out.print("<p><strong>What to do:</strong></p><ul>");
            out.print("<li><strong>Access denied</strong> &rarr; Set MySQL root password in <code>WEB-INF/web.xml</code> (<code>db.password</code>), restart Tomcat.</li>");
            out.print("<li><strong>Unknown database 'oceanview_db'</strong> &rarr; Run <code>mysql -u root -p &lt; database/schema.sql</code> from project root.</li>");
            out.print("<li><strong>Communications link failure</strong> &rarr; Start MySQL (e.g. <code>brew services start mysql</code>).</li>");
            out.print("<li><strong>Table 'users' doesn't exist</strong> &rarr; Run <code>database/schema.sql</code>.</li>");
            out.print("</ul>");
            out.print("<p><a href=\"" + ctx + "/register.jsp\">Back to Register</a></p>");
        }

        out.print("</body></html>");
        out.flush();
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
