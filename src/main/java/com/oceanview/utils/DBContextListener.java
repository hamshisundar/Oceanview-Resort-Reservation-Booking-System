package com.oceanview.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

/**
 * Initializes DBConnection from context params when the application starts.
 */
public class DBContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        var ctx = sce.getServletContext();
        DBConnection.init(ctx);
        // Cache busting: new URL for CSS/JS on every app start so redeployed static files are loaded
        ctx.setAttribute("resourceVersion", String.valueOf(System.currentTimeMillis()));
        // So you can verify in the browser that the new app is deployed (see register page footer)
        ctx.setAttribute("appVersion", "2");

        // Verify DB connection at startup so registration/DB errors are visible in Tomcat logs
        try {
            try (var conn = DBConnection.getConnection()) {
                ctx.setAttribute("dbReady", Boolean.TRUE);
                System.out.println("[OceanView] Database connection: OK");
            }
        } catch (SQLException e) {
            ctx.setAttribute("dbReady", Boolean.FALSE);
            System.err.println("[OceanView] Database connection FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
