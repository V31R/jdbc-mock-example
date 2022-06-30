
import java.util.TreeMap;
import  java.sql.*;

public class Main {

    TreeMap<String, Integer> vacancies = new TreeMap<>();

    public static void main(String[] args) throws SQLException{

        Connection conn = DriverManager.getConnection("jdbc:wm://localhost:8080/sql-mock");

        System.out.println(conn!=null);

    }

}
