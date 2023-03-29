import static org.junit.Assert.*;

import TTest.TTest;
import org.junit.Test;
import statsVisualiser.DataQuery;

import java.sql.SQLException;
import java.util.HashMap;

public class  TTestTest {

    @Test
    public void runTTestReturnsRejectsNullHypothesis() throws Exception {
        HashMap<String, String> param1 = new HashMap<>();
        param1.put("province", "All");
        param1.put("city", "All");
        param1.put("fromYear", "All");
        param1.put("fromMonth", "All");
        param1.put("toYear", "All");
        param1.put("toMonth", "All");

        HashMap<String, String> param2 = new HashMap<>();
        param2.put("province", "Newfoundland and Labrador");
        param2.put("city", "All");
        param2.put("fromYear", "All");
        param2.put("fromMonth", "All");
        param2.put("toYear", "All");
        param2.put("toMonth", "All");

        assertEquals(TTest.runTTest(param1, param2),
                "We cannot reject the null hypothesis. p-value: 0.6892124678826561");
    }
}
