package org.wael;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/todo_list_db";    //my MySQL database location
    private static final String JDBC_USER = "root";                                       //my user name
    private static final String JDBC_PASSWORD = "Cr7_76190235";                           //my databse password

    public static Connection connect() {        //call the imported class sql.connection methods to start connection
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            System.out.println("Connected to the database!");
            return connection;
        } catch (SQLException e) {              //protect from exceptions
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void disconnect(Connection connection) {      //disconnect
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from the database.");
            } catch (SQLException e) {
                System.err.println("Error closing the database connection: " + e.getMessage());
            }
        }
    }
}
