package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection{
    private static DatabaseConnection dbCon;
    private final Connection con;

    private DatabaseConnection() throws ClassNotFoundException,SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con=DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/hardware",
                "root",
                "1234"
        );
    }
    public static DatabaseConnection getInstance() throws SQLException, ClassNotFoundException {
        return dbCon==null ? dbCon=new DatabaseConnection() : dbCon;
    }

    public static void getDatabaseConnection() {
    }

    public Connection getConnection(){
        return con;
    }
}
