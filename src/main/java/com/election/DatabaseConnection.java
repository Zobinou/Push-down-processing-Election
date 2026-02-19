package com.election;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage the PostgreSQL database connection.
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:postgresql://localhost:5432/election_db";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "050107";

    private DatabaseConnection() {
        // Utility class – no instantiation
    }

    /**
     * Opens and returns a new connection to the PostgreSQL database.
     *
     * @return a live {@link Connection}
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}