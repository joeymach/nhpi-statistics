import static org.junit.Assert.*;

import TTest.TTest;
import org.junit.Test;
import weka.TimeSeriesForecast;

import java.util.HashMap;
import java.util.List;

public class TimeSeriesForecastTest {

    @Test
    public void getForecastsSuccess() throws Exception {
        TimeSeriesForecast params = new TimeSeriesForecast("2022-12-01",15,
                1, 1, 1);

        List<Double> forecasts = forecasts = TimeSeriesForecast.getForecasts(params);
        assertEquals(forecasts.size(), 15);
    }

    @Test
    public void getEvaluationMetricsSuccess() throws Exception {
        TimeSeriesForecast params = new TimeSeriesForecast("2022-12-01",15,
                1, 1, 1);

        List<Double> forecasts = TimeSeriesForecast.getForecasts(params);

        String[][] metrics = TimeSeriesForecast.getEvaluationStats();

        String[] evaluationNames = {"Accuracy", "Mean Error", "Model Fitness"};

        for (int index = 0; index < metrics.length; index++) {
            assertEquals(metrics[index][0], evaluationNames[index]);
        }
    }
}
