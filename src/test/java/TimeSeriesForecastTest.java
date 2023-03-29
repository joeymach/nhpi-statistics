import static org.junit.Assert.*;

import TTest.TTest;
import org.junit.Test;
import weka.TimeSeriesForecast;

import java.util.HashMap;
import java.util.List;

public class TimeSeriesForecastTest {

    @Test
    public void getForecastsSuccess() throws Exception {
        List<Double> forecasts = TimeSeriesForecast.getForecasts("2022-12-01",15,
                1, 1, 1);
        assertEquals(forecasts.size(), 15);
    }

    @Test
    public void getEvaluationMetricsSuccess() throws Exception {
        List<Double> forecasts = TimeSeriesForecast.getForecasts("2022-12-01",15,
                1, 1, 1);

        String[][] metrics = TimeSeriesForecast.getEvaluationStats();

        String[] evaluationNames = {"Accuracy", "Mean Error", "Model Fitness"};

        for (int index = 0; index < metrics.length; index++) {
            assertEquals(metrics[index][0], evaluationNames[index]);
        }
    }
}
