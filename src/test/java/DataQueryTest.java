import static org.junit.Assert.*;
import org.junit.Test;
import statsVisualiser.DataQuery;

import java.sql.SQLException;

public class DataQueryTest {
    @Test
    public void getDataFromDatabaseFailsTest() throws SQLException {

        String[][] data = DataQuery.getDataFromDatabase("All", "All",
                "2010", "1", "2000", "1");

        assertEquals(data[0][0], "Invalid");
    }

    @Test
    public void getDataFromDatabaseSucceedsTest1() throws SQLException {
        String[][] data = DataQuery.getDataFromDatabase("All", "All",
                "2010", "1", "2011", "1");

        assertEquals(data.length, 1560);
    }

    @Test
    public void getDataFromDatabaseSucceedsTest2() throws SQLException {
        String[][] data = DataQuery.getDataFromDatabase("All", "All",
                "All", "All", "All", "All");

        assertEquals(data.length, 60480);
    }

    @Test
    public void getDataFromDatabaseSucceedsTest3() throws SQLException {
        String[][] data = DataQuery.getDataFromDatabase("Ontario", "Toronto",
                "2010", "1", "2010", "7");

        assertEquals(data.length, 21);
    }
}
