package weka;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.timeseries.WekaForecaster;

public class ForecastBuilderConcrete implements ForecastBuilder {
    private int epoch = 1;
    private int iteration = 1;
    private int convergenceThreshold = 1;

    public ForecastBuilderConcrete setEpoch(int epoch) {
        this.epoch = epoch;
        return this;
    }

    public ForecastBuilderConcrete setIteration(int iteration) {
        this.iteration = iteration;
        return this;
    }

    public ForecastBuilderConcrete setConvergenceThreshold(int convergenceThreshold) {
        this.convergenceThreshold = convergenceThreshold;
        return this;
    }

    public WekaForecaster buildForecaster() throws Exception {
        WekaForecaster forecaster = new WekaForecaster();
        forecaster.setFieldsToForecast("Value");
        MultilayerPerceptron mlp = new MultilayerPerceptron();

        mlp.setTrainingTime(epoch); // set epoch
        mlp.setNumDecimalPlaces(iteration); // set iteration
        mlp.setValidationThreshold(convergenceThreshold); // set convergence threshold

        forecaster.setBaseForecaster(mlp);

        forecaster.getTSLagMaker().setTimeStampField("Date");
        forecaster.getTSLagMaker().setMinLag(1);
        forecaster.getTSLagMaker().setMaxLag(12);

        forecaster.getTSLagMaker().setAddMonthOfYear(true);
        forecaster.getTSLagMaker().setAddQuarterOfYear(true);

        return forecaster;
    }
}
