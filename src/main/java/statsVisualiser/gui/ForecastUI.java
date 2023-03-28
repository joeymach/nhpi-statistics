package statsVisualiser.gui;

import TTest.Main;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.general.Series;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import weka.TimeSeriesForecast;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class ForecastUI {
    static TimeSeriesCollection dataset = new TimeSeriesCollection();

    public static void showForecast(int timeSeriesLegendIndex, int toYear, int toMonth,
                                    List<Double> forecasts) throws Exception {
        setDataset(timeSeriesLegendIndex, toYear, toMonth, forecasts);
        ChartPanel forecastedChartPanel = getTimeSeriesPanel();
        JScrollPane metricsPanel = getMetricsTable();

        JPanel forecastPanel = new JPanel(new BorderLayout());
        forecastPanel.add(metricsPanel, BorderLayout.CENTER);
        forecastPanel.add(forecastedChartPanel, BorderLayout.SOUTH);
        forecastPanel.setPreferredSize(new Dimension(1000, 600));

        MainUI.getForecastPanel().removeAll();
        MainUI.getForecastPanel().add(forecastPanel);
        MainUI.getInstance().setVisible(true);
    }

    public static ChartPanel getTimeSeriesPanel() {
        XYPlot plot = new XYPlot();
        JFreeChart initialTimeSeriesChart = new JFreeChart("NHPI % Change Monthly",
                new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
        ChartPanel chartPanel = new ChartPanel(initialTimeSeriesChart);
        chartPanel.setPreferredSize(new Dimension(1000, 500));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.white);
        MainUI.getTimeSeriesPanel().add(chartPanel);

        plot.setDataset(0, dataset);
        plot.setRenderer(0, new XYSplineRenderer());
        DateAxis domainAxis = new DateAxis("Month, Year");
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(new NumberAxis("NHPI % Change Monthly with Foreasts"));

        plot.getRenderer().setSeriesStroke(0, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        plot.getRenderer().setSeriesPaint(0, Color.RED);

        plot.getRenderer().setSeriesStroke(1, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{10}, 0));
        plot.getRenderer().setSeriesPaint(1, Color.BLUE);

        return chartPanel;
    }

    public static void setDataset(int timeSeriesLegendIndex, int toYear, int toMonth,
                                  List<Double> forecasts) throws CloneNotSupportedException {
        TimeSeries seriesOriginal = TimeSeriesUI.getTimeSeriesDataset().getSeries(timeSeriesLegendIndex);
        TimeSeries seriesOriginalCopy = (TimeSeries) seriesOriginal.clone();

        seriesOriginalCopy.setKey("Original Data");

        dataset.addSeries(seriesOriginalCopy);

        TimeSeries seriesForecast = new TimeSeries("Forecasted Data");
        int lastIndex = seriesOriginalCopy.getItemCount() - 1;
        double lastValue = (double) seriesOriginalCopy.getDataItem(lastIndex).getValue();
        seriesForecast.add(new Month(toMonth, toYear), lastValue);

        int year = toYear;
        int month = toMonth;
        for(Double forecast : forecasts) {
            if(month >= 12) {
                month = 1;
                year += 1;
            } else {
                month += 1;
            }
            seriesForecast.add(new Month(month, year), forecast);
        }
        dataset.addSeries(seriesForecast);
    }

    public static JScrollPane getMetricsTable() throws Exception {
        String[] columnNames = {"Prediction Statistics", "Value"};

        String[][] evaluationMetrics = TimeSeriesForecast.getEvaluationStats();

        DefaultTableModel model = new DefaultTableModel(evaluationMetrics, columnNames);
        JTable metricsTable = new JTable(model);

        metricsTable.setPreferredScrollableViewportSize(new Dimension(1000, 100));
        metricsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        metricsTable.getColumnModel().getColumn(1).setPreferredWidth(800);

        JScrollPane scrollPane = new JScrollPane(metricsTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }
}
