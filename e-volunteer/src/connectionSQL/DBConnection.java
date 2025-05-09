package connectionSQL;

import java.sql.*;

public class DBConnection {
    private static Connection con;

    private DBConnection() {}

    public static void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/e-volunteer?user=root&password");
            System.out.println("Database connection successful.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    public static Connection getConnection(){
        return con;
    }
}