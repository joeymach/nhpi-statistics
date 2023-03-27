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

    public static void main(String[] args) throws Exception {
        Instances nhpiTrain = new Instances(new BufferedReader(new FileReader("nhpi_avg_date.arff")));

        WekaForecaster forecaster = new WekaForecaster();

        forecaster.setFieldsToForecast("Value");
        MultilayerPerceptron mlp = new MultilayerPerceptron();
        mlp.setTrainingTime(200); // epoch
        mlp.setNumDecimalPlaces(10); // iterations
        mlp.setValidationThreshold(300); // convergence

        forecaster.setBaseForecaster(mlp);

        forecaster.getTSLagMaker().setTimeStampField("Date");
        forecaster.getTSLagMaker().setMinLag(1);
        forecaster.getTSLagMaker().setMaxLag(2);

        forecaster.getTSLagMaker().setAddMonthOfYear(true);
        forecaster.getTSLagMaker().setAddQuarterOfYear(true);

        forecaster.buildForecaster(nhpiTrain, System.out);
        forecaster.primeForecaster(nhpiTrain);

        List<List<NumericPrediction>> forecast = forecaster.forecast(108, System.out);

        for (int i = 0; i < 108; i++) {
            List<NumericPrediction> predsAtStep = forecast.get(i);
            NumericPrediction predForTarget = predsAtStep.get(0);
            System.out.print("" + predForTarget.predicted() + " ");
            System.out.println();
        }
    }

    /*
    Input: String dateToForecastFrom, int numMonthsToForecast, int epoch, int iteration, int convergenceThreshold
    Output: HashMap with 1 element containing (key, value) pair of (WekaForecaster, futureForecastPredictions)
    Gets the forecaster model and the future forecasts for time series
    */
    public static List<Double> getForecasts(String dateToForecastFrom, int numMonthsToForecast,
                                                           int epoch, int iteration, int convergenceThreshold)
                                                           throws Exception {
        Instances nhpiData = TimeSeriesForecast.getPrimingData(dateToForecastFrom);

        forecaster = new WekaForecaster();
        forecaster.setFieldsToForecast("Value");
        MultilayerPerceptron mlp = new MultilayerPerceptron();
        mlp.setTrainingTime(epoch); // set epoch
        mlp.setNumDecimalPlaces(iteration); // set iteration
        mlp.setValidationThreshold(convergenceThreshold); // set convergence threshold

        forecaster.setBaseForecaster(mlp);

        forecaster.getTSLagMaker().setTimeStampField("Date");
        forecaster.getTSLagMaker().setMinLag(1);
        forecaster.getTSLagMaker().setMaxLag(2);

        forecaster.getTSLagMaker().setAddMonthOfYear(true);
        forecaster.getTSLagMaker().setAddQuarterOfYear(true);

        forecaster.buildForecaster(nhpiData, System.out);
        forecaster.primeForecaster(nhpiData);

        List<List<NumericPrediction>> forecast = forecaster.forecast(numMonthsToForecast, System.out);

        List<Double> forecastResult = new ArrayList<>();

        for (int i = 0; i < numMonthsToForecast; i++) {
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
    public static List<Double> getEvaluationStats() throws Exception {
        Instances nhpiTrain = new Instances(new BufferedReader(new FileReader("nhpi_avg_date_train.arff")));
        Instances nhpiTest = new Instances(new BufferedReader(new FileReader("nhpi_avg_date_test.arff")));

        forecaster.buildForecaster(nhpiTrain, System.out);
        forecaster.primeForecaster(nhpiTrain);

        List<List<NumericPrediction>> forecast = forecaster.forecast(108, System.out);

        List<TSEvalModule> evaluationModules = new ArrayList<>();
        TSEvalModule mape = new MAPEModule();
        TSEvalModule mae = new MAEModule();
        TSEvalModule rmse = new RMSEModule();
        evaluationModules.add(mape);
        evaluationModules.add(mae);
        evaluationModules.add(rmse);
        List<Double> evaluationResult = new ArrayList<>();
        for (TSEvalModule mod : evaluationModules) {
            int index = 0;
            List<String> fields = new ArrayList<>();
            fields.add("Value");
            mod.setTargetFields(fields);
            for(Instance i : nhpiTest) {
                mod.evaluateForInstance(forecast.get(index), i);
                index += 1;
            }
            double[] measure = mod.calculateMeasure();
            if ((mod.getEvalName()).equals("MAPE")) {
                evaluationResult.add(Math.round((100.0 - measure[0]) * 100.0) / 100.0);
            } else {
                evaluationResult.add(Math.round((measure[0])* 100.0)/100.0);
            }
            // System.out.println(evaluationResult.get(evaluationResult.size() - 1));
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
