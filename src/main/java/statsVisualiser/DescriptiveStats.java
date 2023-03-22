package statsVisualiser;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.List;

public class DescriptiveStats {
    public static List<Double> getDescriptiveStats(List<Double> nhpiLoadedValues) {
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        nhpiLoadedValues.forEach(descriptiveStatistics::addValue);

        double average = descriptiveStatistics.getMean();
        double median = descriptiveStatistics.getPercentile(50);
        double standardDeviation = descriptiveStatistics.getStandardDeviation();
        double min = descriptiveStatistics.getMin();
        double max = descriptiveStatistics.getMax();

        List<Double> descriptiveStats = new ArrayList<>();
        descriptiveStats.add(Math.round(average * 10.0) / 10.0);
        descriptiveStats.add(Math.round(median * 10.0) / 10.0);
        descriptiveStats.add(Math.round(standardDeviation * 10.0) / 10.0);
        descriptiveStats.add(Math.round(min * 10.0) / 10.0);
        descriptiveStats.add(Math.round(max * 10.0) / 10.0);

        return descriptiveStats;
    }
}
