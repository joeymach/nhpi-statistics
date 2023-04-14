package statsVisualiser.gui;

import java.util.List;

public class ForecastData {
    private final int timeSeriesLegendIndex;
    private final int toYear;
    private final int toMonth;
    private final List<Double> forecasts;

    public ForecastData(int timeSeriesLegendIndex, int toYear, int toMonth, List<Double> forecasts) {
        this.timeSeriesLegendIndex = timeSeriesLegendIndex;
        this.toYear = toYear;
        this.toMonth = toMonth;
        this.forecasts = forecasts;
    }

    public int getTimeSeriesLegendIndex() {
        return timeSeriesLegendIndex;
    }

    public int getToYear() {
        return toYear;
    }

    public int getToMonth() {
        return toMonth;
    }

    public List<Double> getForecasts() {
        return forecasts;
    }
}

