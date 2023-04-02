package statsVisualiser.Stats;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

public class DescriptiveStats extends Statistics {

    // Get an array of descriptive statistics for data (avg, median, st.d, min, max)
    public static String[][] getDescriptiveStats(String[][] rawData) {
        DescriptiveStatistics descriptiveStatistics = createDescriptiveStatsData(rawData);

        double average = descriptiveStatistics.getMean();
        double median = descriptiveStatistics.getPercentile(50);
        double standardDeviation = descriptiveStatistics.getStandardDeviation();
        double min = descriptiveStatistics.getMin();
        double max = descriptiveStatistics.getMax();

        String[][] descriptiveStats = new String[5][2];

        descriptiveStats[0] = new String[]{"Average", Double.toString(Math.round(average * 10.0) / 10.0)};
        descriptiveStats[1] = new String[]{"Median", Double.toString(Math.round(median * 10.0) / 10.0)};
        descriptiveStats[2] = new String[]{"Standard Deviation", Double.toString(Math.round(standardDeviation * 10.0) / 10.0)};
        descriptiveStats[3] = new String[]{"Min", Double.toString(Math.round(min * 10.0) / 10.0)};
        descriptiveStats[4] = new String[]{"Max", Double.toString(Math.round(max * 10.0) / 10.0)};

        return descriptiveStats;
    }
}
