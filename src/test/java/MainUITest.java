import static org.junit.Assert.*;
import org.junit.Test;
import statsVisualiser.Utils;
import statsVisualiser.gui.MainUI;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

public class MainUITest {
    @Test
    public void getDataFromDatabaseFailsTest() throws SQLException {
        MainUI mainUI = new MainUI();
        String[][] data = mainUI.getDataFromDatabase("All", "All",
                "2010", "1", "2000", "1");

        assertEquals(data.length, 0);
    }

    @Test
    public void getDataFromDatabaseSucceedsTest1() throws SQLException {
        MainUI mainUI = new MainUI();
        String[][] data = mainUI.getDataFromDatabase("All", "All",
                "2010", "1", "2011", "1");

        assertEquals(data.length, 1560);
    }

    @Test
    public void getDataFromDatabaseSucceedsTest2() throws SQLException {
        MainUI mainUI = new MainUI();
        String[][] data = mainUI.getDataFromDatabase("All", "All",
                "All", "All", "All", "All");

        assertEquals(data.length, 60480);
    }

    @Test
    public void getDataFromDatabaseSucceedsTest3() throws SQLException {
        MainUI mainUI = new MainUI();
        String[][] data = mainUI.getDataFromDatabase("Ontario", "Toronto",
                "2010", "1", "2010", "7");

        assertEquals(data.length, 21);
    }
}