package statsVisualiser.gui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import statsVisualiser.DataQuery;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TimeSeriesUI {

    static ArrayList<HashMap<String, String>> timeSeriesParams = new ArrayList<>();

    // Data variables
    static TimeSeriesCollection timeSeriesDataset = new TimeSeriesCollection();

    static ArrayList<String[][]> dataListForTimeSeries = new ArrayList<String[][]>();

    public static int numOfTimeSeries = 0;

    public static void addTimeSeries(String province, String city, String fromYear,
                                     String fromMonth, String toYear, String toMonth) {
        addTimeSeriesParams(province, city, fromYear, fromMonth, toYear, toMonth);

        String[][] data = new String[1][1];
        try {
            data = DataQuery.getDataFromDatabase(province, city, fromYear,
                    fromMonth, toYear, toMonth);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (data[0][0].equals("Invalid")) {
            // Error alert window shown when selection parameters are invalid
            JOptionPane.showMessageDialog(MainUI.getInstance(), "Invalid parameters, please choose again.");
        } else {
            addTimeSeriesToDataset(data);

            ChartPanel chartPanel = getTimeSeriesChartPanel();
            MainUI.getTimeSeriesPanel().removeAll();
            MainUI.getTimeSeriesPanel().add(chartPanel);
            MainUI.getInstance().setVisible(true);
        }

    }

    public static void addTimeSeriesParams(String province, String city, String fromYear,
                                           String fromMonth, String toYear, String toMonth) {
        HashMap<String, String> timeSeriesParam = new HashMap<>();
        timeSeriesParam.put("province", province);
        timeSeriesParam.put("city", city);
        timeSeriesParam.put("fromYear", fromYear);
        timeSeriesParam.put("fromMonth", fromMonth);
        timeSeriesParam.put("toYear", toYear);
        timeSeriesParam.put("toMonth", toMonth);
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
        XYPlot timeSeriesPlot = new XYPlot();
        JFreeChart initialTimeSeriesChart = new JFreeChart("NHPI % Change Monthly",
                new Font("Serif", java.awt.Font.BOLD, 18), timeSeriesPlot, true);
        ChartPanel chartPanel = new ChartPanel(initialTimeSeriesChart);
        chartPanel.setPreferredSize(new Dimension(1000, 600));
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
}
