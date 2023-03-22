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
        descriptiveStats.add(average);
        descriptiveStats.add(median);
        descriptiveStats.add(standardDeviation);
        descriptiveStats.add(min);
        descriptiveStats.add(max);

        return descriptiveStats;
    }
}
