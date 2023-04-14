import org.junit.Test;
import statsVisualiser.DataQuery;
import statsVisualiser.Stats.DescriptiveStats;

import static org.junit.Assert.assertEquals;

public class DescriptiveStatsTest {

    @Test
    public void getDescriptiveStatsSuccess() throws Exception {
        DataQuery query = new DataQuery("All", "All",
                "2000", "1", "2010", "1");
        String[][] data = DataQuery.getDataFromDatabase(query);

        String[][] descriptiveStats = DescriptiveStats.getDescriptiveStats(data);

        String[][] result = new String[5][2];

        result[0] = new String[]{"Average", "63.4"};
        result[1] = new String[]{"Median", "72.2"};
        result[2] = new String[]{"Standard Deviation", "31.3"};
        result[3] = new String[]{"Min", "0.0"};
        result[4] = new String[]{"Max", "138.1"};

        for(int index = 0; index < descriptiveStats.length; index++) {
            assertEquals(descriptiveStats[index][0], result[index][0]);
            assertEquals(descriptiveStats[index][1], result[index][1]);
        }
    }
}
