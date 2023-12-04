import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MYSQLUtil {

    public static Connection getConnection() throws SQLException {
        Connection conn = null;

        try {
            String url = "";
            String user = "root";
            String password = "";

            conn = DriverManager.getConnection(url, user, password);

            
        } catch(SQLException e) {
                System.out.println(e.getMessage())
        }
        return conn;
        
    }
}