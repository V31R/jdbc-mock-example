
import  java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException{

        Connection conn = DriverManager.getConnection("jdbc:wm://localhost:8080/sql-mock");

        System.out.println(conn!=null);

    }

}
