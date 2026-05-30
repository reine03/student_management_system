package com.student;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // ─────────────────────────────────────────────────────────────
    //  !! CHANGE THESE VALUES TO MATCH YOUR POSTGRESQL SETUP !!
    private static final String URL      = "jdbc:postgresql://localhost:5432/studentdb";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "your_password";   // <-- put your password here
    // ─────────────────────────────────────────────────────────────

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
