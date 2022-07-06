package kalchenko.straightUseDriverTest;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.Suite;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;

@Suite
public class SmokeDriverTest {

    private static String url="jdbc:wm://localhost:8080/sql-mock";

    @Test
    public void driverIsLoaded() throws SQLException{

        Driver driver = DriverManager.getDriver(url);

        assertNotNull(driver);

    }

    @Test
    public void loadConnection() throws SQLException{

        Connection connection = DriverManager.getConnection(url);

        assertNotNull(connection);

    }

    @Test
    public void getStatement() throws SQLException{

        Connection connection = DriverManager.getConnection(url);

        Statement statement = connection.createStatement();

        assertNotNull(statement);

    }

}
