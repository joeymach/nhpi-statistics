package statsVisualiser.Stats;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
    public static List<Double> sanitizeNhpiInput(String[][] rawData) {
        List<Double> nhpiLoadedValues = new ArrayList<>();
        for(String[] row : rawData) {
            nhpiLoadedValues.add(Double.parseDouble(row[4]));
        }
        return nhpiLoadedValues;
    }

    public static DescriptiveStatistics createDescriptiveStatsData(String[][] rawData) {
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        List<Double> nhpiLoadedValues = sanitizeNhpiInput(rawData);
        nhpiLoadedValues.forEach(descriptiveStatistics::addValue);
        return descriptiveStatistics;
    }
}
