package com.oceanview.utils;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection. Uses only the built-in H2 in-memory database.
 * No MySQL — registration works as soon as you deploy.
 */
public class DBConnection {

    private static boolean h2Initialized;

    private static final String H2_URL = "jdbc:h2:mem:oceanview;DB_CLOSE_DELAY=-1;MODE=MySQL;DATABASE_TO_LOWER=TRUE";

    public static void init(ServletContext ctx) {
        h2Initialized = false;
        System.out.println("[OceanView] Using built-in H2 database. No MySQL required.");
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 driver not found", e);
        }
        Connection conn = DriverManager.getConnection(H2_URL, "sa", "");
        if (!h2Initialized) {
            synchronized (DBConnection.class) {
                if (!h2Initialized) {
                    H2Init.initSchema(conn);
                    h2Initialized = true;
                }
            }
        }
        return conn;
    }

    public static boolean isUsingH2() {
        return true;
    }
}
