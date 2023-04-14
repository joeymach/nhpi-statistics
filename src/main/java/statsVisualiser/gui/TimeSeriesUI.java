package statsVisualiser.gui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import statsVisualiser.DataQuery;
import statsVisualiser.ErrorComponents.ErrorUI;
import statsVisualiser.ErrorComponents.ErrorUserParams;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/*
Class responsibility: [Use case 1] compare time series on
demand. Users can select parameters to load a new time series
to be compared with existing time series on demand.
*/
public class TimeSeriesUI {

    static JPanel timeSeriesUIPanel = new JPanel();
    static XYPlot timeSeriesPlot;
    static ChartPanel chartPanel;
    static ArrayList<HashMap<String, String>> timeSeriesParams = new ArrayList<>();

    // Data variables
    static TimeSeriesCollection timeSeriesDataset = new TimeSeriesCollection();

    static ArrayList<String[][]> dataListForTimeSeries = new ArrayList<String[][]>();

    public static int numOfTimeSeries = 0;

    // Adding a new time series to be compared with the other loaded time series graph
    public static void addTimeSeries(HashMap<String, String> timeSeriesParam) {

        String[][] data = new String[1][1];
        try {
            data = DataQuery.getDataFromDatabase(timeSeriesParam.get("province"), timeSeriesParam.get("city"), timeSeriesParam.get("toYear"),
                    timeSeriesParam.get("fromMonth"), timeSeriesParam.get("toYear"), timeSeriesParam.get("toMonth"));

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        ErrorUI error = new ErrorUserParams(data);
        if(error.isValid()) {
            addTimeSeriesParams(timeSeriesParam);
            addTimeSeriesToDataset(data);
            renderTimeSeriesUIPanel();
            TTestUI.initializeTTestPanel();
        }
    }

    public static void renderTimeSeriesUIPanel() {
        timeSeriesUIPanel.removeAll();

        JScrollPane timeSeriesLegend = getTimeSeriesLegend();
        timeSeriesUIPanel.add(timeSeriesLegend);

        if(chartPanel == null) {
            chartPanel = getTimeSeriesChartPanel();
        }
        timeSeriesUIPanel.add(chartPanel);

        timeSeriesUIPanel.add(ForecastSelectionUI.getForecastMenuPanel());

        timeSeriesUIPanel.setLayout(new BoxLayout(timeSeriesUIPanel, BoxLayout.Y_AXIS));

        MainUI.getTimeSeriesPanel().removeAll();
        MainUI.getTimeSeriesPanel().add(timeSeriesUIPanel);
        MainUI.getInstance().setVisible(true);
    }

    public static void addTimeSeriesParams(HashMap<String, String> newTimeSeriesParam ) {
        HashMap<String, String> timeSeriesParam = new HashMap<>(newTimeSeriesParam);
        TimeSeriesUI.timeSeriesParams.add(timeSeriesParam);
    }

    public static void addTimeSeriesToDataset(String[][] data) {
        numOfTimeSeries += 1;
        TimeSeries series = new TimeSeries(Integer.toString(numOfTimeSeries));
        for(String[] row : data) {
            series.addOrUpdate(new Month(Integer.parseInt(row[1]), Integer.parseInt(row[0])), Double.parseDouble(row[4]));
        }
        timeSeriesDataset.addSeries(series);
    }


     public static ChartPanel getTimeSeriesChartPanel() {
        timeSeriesPlot = new XYPlot();
        JFreeChart initialTimeSeriesChart = new JFreeChart("NHPI % Change Monthly",
                new Font("Serif", java.awt.Font.BOLD, 18), timeSeriesPlot, true);
        ChartPanel chartPanel = new ChartPanel(initialTimeSeriesChart);
        chartPanel.setPreferredSize(new Dimension(1000, 500));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.white);
        MainUI.getTimeSeriesPanel().add(chartPanel);

        timeSeriesPlot.setDataset(0, timeSeriesDataset);
        timeSeriesPlot.setRenderer(0, new XYSplineRenderer());
        DateAxis domainAxis = new DateAxis("Month, Year");
        timeSeriesPlot.setDomainAxis(domainAxis);
        timeSeriesPlot.setRangeAxis(new NumberAxis("NHPI % Change Monthly"));

        return chartPanel;
    }

    public static JScrollPane getTimeSeriesLegend() {
        String[] columnNames = {"Time Series Legend #", "Parameters inputted"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        int legendNum = 1;
        for (HashMap<String, String> timeSeriesParam : timeSeriesParams) {
            String paramString = "Province: " + timeSeriesParam.get("province") +
                    ", City: " + timeSeriesParam.get("city") +
                    ", fromYear: " + timeSeriesParam.get("fromYear") +
                    ", fromMonth: " + timeSeriesParam.get("fromMonth") +
                    ", toYear: " + timeSeriesParam.get("toYear") +
                    ", toMonth: " + timeSeriesParam.get("toMonth");
            model.addRow(new String[]{Integer.toString(legendNum), paramString});
            legendNum += 1;
        }

        JTable timeSeriesLegendTable = new JTable(model);

        timeSeriesLegendTable.setPreferredScrollableViewportSize(new Dimension(1000, 80));
        timeSeriesLegendTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        timeSeriesLegendTable.getColumnModel().getColumn(1).setPreferredWidth(850);

        JScrollPane scrollPane = new JScrollPane(timeSeriesLegendTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }

    public static TimeSeriesCollection getTimeSeriesDataset() {
        return timeSeriesDataset;
    }

    public static int getNumOfTimeSeries() {
        return numOfTimeSeries;
    }

    public static ArrayList<HashMap<String, String>> getTimeSeriesParams() {
        return timeSeriesParams;
    }
}
