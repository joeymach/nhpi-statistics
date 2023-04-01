package weka;

import weka.classifiers.timeseries.WekaForecaster;

interface ForecastBuilder {
    // Default forecaster parameters
    int epoch = 1;
    int iteration = 1;
    int convergenceThreshold = 1;

    public ForecastBuilder setEpoch(int epoch);

    public ForecastBuilder setIteration(int iteration);

    public ForecastBuilderConcrete setConvergenceThreshold(int convergenceThreshold);

    public WekaForecaster buildForecaster() throws Exception;
}
