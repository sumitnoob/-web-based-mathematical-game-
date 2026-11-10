package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * TASK 01 / 07 - JDBC connectivity.
 * Reads the connection settings from src/main/resources/db.properties and opens connections.
 * (Environment variables DB_URL, DB_USER, DB_PASSWORD and DB_DRIVER override the file if set.)
 */
public final class DBConnection {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnection() { }

    private static String setting(String envName, String propName, String defaultValue) {
        String fromEnv = System.getenv(envName);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        return PROPS.getProperty(propName, defaultValue);
    }

    /** Opens a new connection. Always close it (use try-with-resources). */
    public static Connection getConnection() throws SQLException {
        try {
            // Loads and registers the JDBC driver
            Class.forName(setting("DB_DRIVER", "db.driver", "com.mysql.cj.jdbc.Driver"));
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC driver not found: " + e.getMessage(), e);
        }
        return DriverManager.getConnection(
                setting("DB_URL", "db.url", "jdbc:mysql://localhost:3306/math_whiz_db"),
                setting("DB_USER", "db.user", "root"),
                setting("DB_PASSWORD", "db.password", ""));
    }
}
