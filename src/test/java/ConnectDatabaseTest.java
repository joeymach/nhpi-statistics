import static org.junit.Assert.*;

import org.junit.Test;

import database.ConnectDatabase;
import java.sql.*;

public class ConnectDatabaseTest {
    @Test
    public void returnsConnectionSuccessfully() throws Exception {
        System.out.println("Tests if a connection object is returned successfully");

        ConnectDatabase connect = new ConnectDatabase();
        // If you've already set up local MySQL and ran ConnectDatabase's main file,
        // you should not run establishConnection() again
        // connect.establishConnection();
        Connection connection = connect.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void getQueryReturnsAppropriateQuery1() {
        System.out.println("Test if getQuery returns appropriate MySQL query");

        String query = ConnectDatabase.getQuery("Ontario", "Toronto", "All",
                "All", "All", "All");

        String expectedQuery = "SELECT * from nhpi WHERE province=\"Ontario\" AND city=\"Toronto\";";
        assertEquals(query, expectedQuery);
    }

    @Test
    public void getQueryReturnsAppropriateQuery2() {
        System.out.println("Test if getQuery returns appropriate MySQL query");

        String query = ConnectDatabase.getQuery("Ontario", "Toronto", "2010",
                "1", "2014", "2");

        String expectedQuery = "SELECT * from nhpi WHERE province=\"Ontario\" AND city=\"Toronto\" AND ((year=2010 AND month>=1) OR year>=2011) AND ((year=2014 AND month<=2) OR year<=2013);";
        assertEquals(query, expectedQuery);
    }

    @Test
    public void getQueryReturnsAppropriateQuery3() {
        System.out.println("Test if getQuery returns appropriate MySQL query");

        String query = ConnectDatabase.getQuery("All", "All", "All",
                "All", "All", "All");

        String expectedQuery = "SELECT * from nhpi;";
        assertEquals(query, expectedQuery);
    }
}

