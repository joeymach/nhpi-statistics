package TTest;

import database.ConnectDatabase;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import statsVisualiser.DataQuery;

import java.sql.*;
import java.util.HashMap;

public class TTest {
    public static void main(String[] args) throws Exception {
        String[][] data1 = DataQuery.getDataFromDatabase("Nova Scotia", "All", "All",
                "All", "All", "All");

        String[][] data2 = DataQuery.getDataFromDatabase("All", "All", "All",
                "All", "All", "All");

        SummaryStatistics statsOne = getSummaryStatistics(data1);
        SummaryStatistics statsTwo = getSummaryStatistics(data2);

        org.apache.commons.math3.stat.inference.TTest tTest = new org.apache.commons.math3.stat.inference.TTest();
        double pValue = tTest.tTest(statsOne, statsTwo);
        System.out.println(pValue);
    }

    public static String runTTest(HashMap<String, String> timeSeries1Param,
                                  HashMap<String, String> timeSeries2Param) throws SQLException {
        String[][] data1 = getDataFromParams(timeSeries1Param);
        String[][] data2 = getDataFromParams(timeSeries2Param);

        SummaryStatistics statsOne = getSummaryStatistics(data1);
        SummaryStatistics statsTwo = getSummaryStatistics(data2);

        org.apache.commons.math3.stat.inference.TTest tTest = new org.apache.commons.math3.stat.inference.TTest();
        double pValue = tTest.tTest(statsOne, statsTwo);

        String result;
        if (pValue < 0.05) {
            result = "We can reject the null hypothesis.";
        } else {
            result = "We cannot reject the null hypothesis.";
        }
        result += " p-value: " + pValue;
        return result;
    }

    public static SummaryStatistics getSummaryStatistics(String[][] data) {
        SummaryStatistics stats = new SummaryStatistics();
        for(String[] row : data) {
            stats.addValue(Double.parseDouble(row[4]));
        }
        return stats;
    }

    public static String[][] getDataFromParams(HashMap<String, String> timeSeriesParam) throws SQLException {
        String[][] data = DataQuery.getDataFromDatabase(timeSeriesParam.get("province"), timeSeriesParam.get("city"),
                timeSeriesParam.get("fromYear"), timeSeriesParam.get("fromMonth"),
                timeSeriesParam.get("toYear"), timeSeriesParam.get("toMonth"));
        return data;
    }
}
