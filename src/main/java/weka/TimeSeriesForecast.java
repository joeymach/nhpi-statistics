package weka;
import weka.*;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.GaussianProcesses;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.classifiers.timeseries.WekaForecaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class TimeSeriesForecast {
    public static void main(String[] args) throws Exception {

//        // Convert CSV data into ARFF data
//        CSVLoader loader = new CSVLoader();
//        loader.setSource(new File("nhpi_avg_date.csv"));
//        Instances data = loader.getDataSet();
//
//        ArffSaver saver = new ArffSaver();
//        saver.setInstances(data);
//        saver.setFile(new File("nhpi_avg_date.arff"));
//        saver.writeBatch();

        Instances nhpiData = new Instances(new BufferedReader(new FileReader("nhpi_avg_date.arff")));

        WekaForecaster forecaster = new WekaForecaster();

        forecaster.setFieldsToForecast("Value");

        forecaster.setBaseForecaster(new GaussianProcesses());

        forecaster.getTSLagMaker().setTimeStampField("Date");
        forecaster.getTSLagMaker().setMinLag(1);
        forecaster.getTSLagMaker().setMaxLag(12);

        forecaster.getTSLagMaker().setAddMonthOfYear(true);
        forecaster.getTSLagMaker().setAddQuarterOfYear(true);

        forecaster.buildForecaster(nhpiData, System.out);
        forecaster.primeForecaster(nhpiData);

        List<List<NumericPrediction>> forecast = forecaster.forecast(1000, System.out);
        for (int i = 0; i < 100; i++) {
            List<NumericPrediction> predsAtStep = forecast.get(i);
            NumericPrediction predForTarget = predsAtStep.get(0);
            System.out.print("" + predForTarget.predicted() + " ");
            System.out.println();
        }

    }
}
