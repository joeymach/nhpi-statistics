import static org.junit.Assert.*;
import org.junit.Test;
import statsVisualiser.DataQuery;

import java.sql.SQLException;

public class DataQueryTest {
    @Test
    public void getDataFromDatabaseFailsTest() throws SQLException {
        DataQuery query = new DataQuery("All", "All",
                "2010", "1", "2000", "1");
        String[][] data = DataQuery.getDataFromDatabase(query);

        assertEquals(data[0][0], "Invalid");
    }

    @Test
    public void successfulParsingOfDataFromCsvToNhpiDatabase() throws SQLException {
        DataQuery query = new DataQuery("All", "All",
                "All", "All", "All", "All");
        String[][] data = DataQuery.getDataFromDatabase(query);

        assertEquals(data.length, 60480);
    }

    @Test
    public void getDataFromDatabaseSucceedsTest1() throws SQLException {
        DataQuery query = new DataQuery("All", "All",
                "2010", "1", "2011", "1");
        String[][] data = DataQuery.getDataFromDatabase(query);

        assertEquals(data.length, 1560);
    }


    @Test
    public void getDataFromDatabaseSucceedsTest2() throws SQLException {
        DataQuery query = new DataQuery("Ontario", "Toronto",
                "2010", "1", "2010", "7");
        String[][] data = DataQuery.getDataFromDatabase(query);

        assertEquals(data.length, 21);
    }
}
