
package controlStock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseManager {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mundo_computacion", "root", "admin");
            } catch (SQLException e) {
                System.out.println("Falló la conexión");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
