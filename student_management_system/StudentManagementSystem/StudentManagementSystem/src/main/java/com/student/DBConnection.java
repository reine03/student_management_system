package com.student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/studentdb";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "";
    private static final String PLACEHOLDER_PASSWORD = "your_password";
    private static final Properties APPLICATION_PROPERTIES = loadApplicationProperties();

    public static Connection connect() {
        String url = getSetting("db.url", "DB_URL", DEFAULT_URL);
        String user = getSetting("db.user", "DB_USER", DEFAULT_USER);
        String password = getSetting("db.password", "DB_PASSWORD", DEFAULT_PASSWORD);

        if (!hasUsablePassword(password)) {
            return null;
        }

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    private static String getSetting(String propertyName, String environmentName, String defaultValue) {
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        String environmentValue = System.getenv(environmentName);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }

        String fileValue = APPLICATION_PROPERTIES.getProperty(propertyName);
        if (fileValue != null && !fileValue.isBlank()) {
            return fileValue.trim();
        }

        return defaultValue;
    }

    private static Properties loadApplicationProperties() {
        Properties properties = new Properties();

        try (var input = DBConnection.class.getResourceAsStream("/application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception e) {
            System.err.println("Could not read application.properties: " + e.getMessage());
        }

        return properties;
    }

    private static boolean hasUsablePassword(String password) {
        return password != null
                && !password.isBlank()
                && !PLACEHOLDER_PASSWORD.equals(password.trim());
    }
}
