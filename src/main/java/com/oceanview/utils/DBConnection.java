package com.oceanview.utils;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Database connection. Uses built-in H2 file DB for persistence.
 * No MySQL required in local development.
 */
public class DBConnection {

    private static boolean h2Initialized;
    private static boolean useH2 = true;
    private static String mysqlUrl = "";
    private static String mysqlUser = "";
    private static String mysqlPassword = "";

    private static final String H2_URL = buildPersistentH2Url();

    public static void init(ServletContext ctx) {
        h2Initialized = false;
        String forceH2Param = ctx != null ? ctx.getInitParameter("db.forceH2") : null;
        useH2 = forceH2Param == null || Boolean.parseBoolean(forceH2Param);
        mysqlUrl = valueOrEmpty(ctx != null ? ctx.getInitParameter("db.url") : null);
        mysqlUser = valueOrEmpty(ctx != null ? ctx.getInitParameter("db.user") : null);
        mysqlPassword = valueOrEmpty(ctx != null ? ctx.getInitParameter("db.password") : null);

        if (useH2) {
            System.out.println("[OceanView] DB mode: H2 (forced). URL: " + H2_URL);
        } else {
            System.out.println("[OceanView] DB mode: MySQL. URL: " + mysqlUrl);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (!useH2) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL driver not found", e);
            }
            return DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);
        }

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
        return useH2;
    }

    private static String buildPersistentH2Url() {
        try {
            String home = System.getProperty("user.home");
            Path dir = Paths.get(home, ".oceanview", "data");
            Files.createDirectories(dir);
            Path dbFile = dir.resolve("oceanview");
            return "jdbc:h2:file:" + dbFile.toAbsolutePath() + ";AUTO_SERVER=TRUE;MODE=MySQL;DATABASE_TO_LOWER=TRUE";
        } catch (Exception e) {
            // Fallback to project-local path if user-home path is unavailable.
            return "jdbc:h2:file:./data/oceanview;AUTO_SERVER=TRUE;MODE=MySQL;DATABASE_TO_LOWER=TRUE";
        }
    }

    private static String valueOrEmpty(String s) {
        return s == null ? "" : s.trim();
    }
}
