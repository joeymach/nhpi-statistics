package weka;

import weka.classifiers.timeseries.eval.RMSEModule;
import weka.classifiers.timeseries.eval.MAEModule;
import weka.classifiers.timeseries.eval.MAPEModule;
import weka.classifiers.timeseries.eval.TSEvalModule;

import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.MultilayerPerceptron;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.classifiers.timeseries.WekaForecaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSeriesForecast {
    static WekaForecaster forecaster;
    private String dateToForecastFrom;
    private int numMonthsToForecast;
    private int epoch;
    private int iteration;
    private int convergenceThreshold;

    public TimeSeriesForecast(String dateToForecastFrom, int numMonthsToForecast, int epoch, int iteration, int convergenceThreshold) {
        this.dateToForecastFrom = dateToForecastFrom;
        this.numMonthsToForecast = numMonthsToForecast;
        this.epoch = epoch;
        this.iteration = iteration;
        this.convergenceThreshold = convergenceThreshold;
    }

    public String getDateToForecastFrom() {
        return dateToForecastFrom;
    }

    public int getNumMonthsToForecast() {
        return numMonthsToForecast;
    }

    public int getEpoch() {
        return epoch;
    }

    public int getIteration() {
        return iteration;
    }

    public int getConvergenceThreshold() {
        return convergenceThreshold;
    }
    public static void main(String[] args) throws Exception {
        Instances nhpiTrain = new Instances(new BufferedReader(new FileReader("nhpi_avg_date.arff")));

        WekaForecaster forecaster = new ForecastBuilderConcrete().buildForecaster();

        forecaster.buildForecaster(nhpiTrain, System.out);
        forecaster.primeForecaster(nhpiTrain);

        List<List<NumericPrediction>> forecast = forecaster.forecast(108);

        for (int i = 0; i < 108; i++) {
            List<NumericPrediction> predsAtStep = forecast.get(i);
            NumericPrediction predForTarget = predsAtStep.get(0);
            System.out.println(predForTarget.predicted() + " ");
        }
    }

    /*
    Input: String dateToForecastFrom, int numMonthsToForecast, int epoch, int iteration, int convergenceThreshold
    Output: HashMap with 1 element containing (key, value) pair of (WekaForecaster, futureForecastPredictions)
    Gets the forecaster model and the future forecasts for time series
    */
//    public static List<Double> getForecasts(String dateToForecastFrom, int numMonthsToForecast,
//                                                           int epoch, int iteration, int convergenceThreshold)
//                                                           throws Exception {
//        Instances nhpiData = TimeSeriesForecast.getPrimingData(dateToForecastFrom);
//
//        forecaster = new ForecastBuilderConcrete()
//                .setEpoch(epoch)
//                .setIteration(iteration)
//                .setConvergenceThreshold(convergenceThreshold)
//                .buildForecaster();
//
//        forecaster.buildForecaster(nhpiData);
//        forecaster.primeForecaster(nhpiData);
//
//        List<List<NumericPrediction>> forecast = forecaster.forecast(numMonthsToForecast, System.out);
//
//        List<Double> forecastResult = new ArrayList<>();
//
//        System.out.println("Forecast Results:");
//        for (int i = 0; i < numMonthsToForecast; i++) {
//            List<NumericPrediction> predsAtStep = forecast.get(i);
//            NumericPrediction predForTarget = predsAtStep.get(0);
//            forecastResult.add(Math.round(predForTarget.predicted() * 100.00) / 100.00);
//            System.out.println(predForTarget.predicted());
//        }
//
//        return forecastResult;
//    }

    public static List<Double> getForecasts(TimeSeriesForecast forecastParams) throws Exception {
        Instances nhpiData = TimeSeriesForecast.getPrimingData(forecastParams.getDateToForecastFrom());

        forecaster = new ForecastBuilderConcrete()
                .setEpoch(forecastParams.getEpoch())
                .setIteration(forecastParams.getIteration())
                .setConvergenceThreshold(forecastParams.getConvergenceThreshold())
                .buildForecaster();

        forecaster.buildForecaster(nhpiData);
        forecaster.primeForecaster(nhpiData);

        List<List<NumericPrediction>> forecast = forecaster.forecast(forecastParams.getNumMonthsToForecast(), System.out);

        List<Double> forecastResult = new ArrayList<>();

        System.out.println("Forecast Results:");
        for (int i = 0; i < forecastParams.getNumMonthsToForecast(); i++) {
            List<NumericPrediction> predsAtStep = forecast.get(i);
            NumericPrediction predForTarget = predsAtStep.get(0);
            forecastResult.add(Math.round(predForTarget.predicted() * 100.00) / 100.00);
            System.out.println(predForTarget.predicted());
        }

        return forecastResult;
    }


    /*
    Input: WekaForecaster forecaster
    Output: List<Double> with 3 elements which correspond to values for model accuracy, mean error, model fitness
    Gets forecaster evaluation stats: accuracy, mean error, model fitness
    */
    public static String[][] getEvaluationStats() throws Exception {
        String[][] evaluationResult = createForecastModResult();
        return evaluationResult;
    }

    private static double[] getEvalPerForecastMod(TSEvalModule module) throws Exception {
        Instances nhpiTrain = new Instances(new BufferedReader(new FileReader("nhpi_avg_date_train.arff")));
        Instances nhpiTest = new Instances(new BufferedReader(new FileReader("nhpi_avg_date_test.arff")));

        forecaster.buildForecaster(nhpiTrain, System.out);
        forecaster.primeForecaster(nhpiTrain);

        List<List<NumericPrediction>> forecast = forecaster.forecast(108, System.out);

        List<String> fields = new ArrayList<>();
        fields.add("Value");
        module.setTargetFields(fields);

        int index = 0;
        for(Instance i : nhpiTest) {
            module.evaluateForInstance(forecast.get(index), i);
            index += 1;
        }
        return module.calculateMeasure();
    }

    private static String[][] createForecastModResult() throws Exception {
        List<TSEvalModule> evaluationModules = new ArrayList<>();
        TSEvalModule mape = new MAPEModule();
        TSEvalModule mae = new MAEModule();
        TSEvalModule rmse = new RMSEModule();
        evaluationModules.add(mape);
        evaluationModules.add(mae);
        evaluationModules.add(rmse);

        String[] evaluationNames = {"Accuracy", "Mean Error", "Model Fitness"};
        String[][] evaluationResult = new String[3][2];
        int evalIndex = 0;
        for (TSEvalModule module : evaluationModules) {
            double[] measure = getEvalPerForecastMod(module);
            if ((module.getEvalName()).equals("MAPE")) {
                evaluationResult[evalIndex] = new String[]{evaluationNames[evalIndex],
                        Double.toString(Math.round((100.0 - measure[0]) * 100.0) / 100.0)};
            } else {
                evaluationResult[evalIndex] = new String[]{evaluationNames[evalIndex],
                        Double.toString(Math.round((measure[0])* 100.0)/100.0)};
            }
            // System.out.println(evaluationResult.get(evaluationResult.size() - 1));
            evalIndex += 1;
        }
        return evaluationResult;
    }

    /*
    Input: toDate in format String "YYYY-MM-DD"
    Output: data in dataset up until toDate
    */
    public static Instances getPrimingData(String toDate) throws Exception {
        long monthsBetween = ChronoUnit.MONTHS.between(
                YearMonth.from(LocalDate.parse(toDate)),
                YearMonth.from(LocalDate.parse("2022-12-01"))
        );

        System.out.println(monthsBetween);

        Instances nhpiData = new Instances(new BufferedReader(new FileReader("nhpi_avg_date.arff")));

        // filter data set to all data points dated before input date
        long startIndex = nhpiData.size() - monthsBetween;
        for (int i = nhpiData.size() - 1; i >= startIndex; i--) {
            nhpiData.remove(i);
        }

        // return the filtered dataset
        return nhpiData;
    }

    /*
    Convert csv file into arff file which WEKA api supports
    */
    public static void convertCsvToArff() throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File("nhpi_avg_date.csv"));
        Instances data = loader.getDataSet();

        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File("nhpi_avg_date.arff"));
        saver.writeBatch();
    }
}
