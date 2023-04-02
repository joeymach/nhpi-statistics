package statsVisualiser.gui;

import database.ConnectDatabase;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import statsVisualiser.DataQuery;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static statsVisualiser.gui.MainUI.midContainer;

/*
Class responsibility: [Use case 3] Renders predefined
visualizations with configurations.
*/
public class Visualizations extends JFrame implements ListSelectionListener {

    private static final long serialVersionUID = 1L;

    JPanel loadedDataPane = new JPanel();
    JPanel panel = new JPanel();

    static JPanel timeSeriesPanel = new JPanel();
    static JPanel forecastPanel = new JPanel();
    //
//	// UI instance variables
    UserParametersUI userParametersUI;

    //Visualizations variables
    JPanel chartLayout = new JPanel();
    String visualizations[] = {"Line Chart", "Bar Chart", "Scatter Chart", "Pie Chart"};
    JList list = new JList(visualizations);

    JFreeChart barchart;

    JFreeChart chart;
    ChartPanel linePanel = new ChartPanel(chart);
    JPanel barPanel = new ChartPanel(chart);

    JFreeChart scatterchart;

    ChartPanel scatterpanel = new ChartPanel(chart);


    JFreeChart piechart;
    ChartPanel piepanel = new ChartPanel(chart);

    //	 MainUI instance
    private static MainUI instance;
    public static JFrame frame;

    String[][] array = new String[0][0];

    int row = 0;
    boolean gridLinesSelected = false;
    Color changeColour = Color.red;
    Shape changeShape = new Ellipse2D.Double(-3, -3, 6, 6);;


    public Visualizations(){
    }

    public JPanel getVisualizations() {
        // 4nd Panel: Visualizations


        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setPreferredSize(new Dimension(200, 200));
        list.addListSelectionListener(this);


        chartLayout.setLayout(new BoxLayout(chartLayout, BoxLayout.X_AXIS));
        chartLayout.setPreferredSize(new Dimension(600, 400));
//        try {
//            linechart();
//            barchart();
//            scatter();
//            piechart();
//        } catch (SQLException ex) {
//            throw new RuntimeException(ex);
//        };
//        chartLayout.add(linePanel);
//        chartLayout.add(barPanel);
//        chartLayout.add(scatterpanel);
//        chartLayout.add(piepanel);
//        linePanel.setVisible(false);
//        barPanel.setVisible(false);
//        scatterpanel.setVisible(false);
//        piepanel.setVisible(false);

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(chartLayout);
        panel.add(list);

        return panel;
    }

    public JPanel getSettingsPanel() {
        // Create a new JPanel to hold the settings UI elements
        JPanel settingsPanel = new JPanel();
        JPanel subPanel = new JPanel();


        // Add UI elements to the settingsPanel, such as JLabels, JTextFields, JButtons, etc.
        // ...
        settingsPanel.setLayout(new GridLayout(2,2));
        settingsPanel.add(new JLabel("Settings",SwingConstants.CENTER));

        JCheckBox gridLines = new JCheckBox("Show Grid Lines");
        gridLines.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                gridLinesSelected = gridLines.isSelected();

            }
        });
        subPanel.add(gridLines);

        JLabel colourLabel = new JLabel("Change Colour");
        subPanel.add(colourLabel);
        String[] colors = {"Red", "Green", "Blue", "Yellow"};
        JComboBox<String> colorComboBox = new JComboBox<String>(colors);
        colorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected color
                String selectedColor = (String) colorComboBox.getSelectedItem();

                // Set the color of the visualization based on the selected color
                if (selectedColor.equals("Red")) {
                    changeColour = Color.red;
                } else if (selectedColor.equals("Green")) {
                    changeColour = Color.GREEN;
                } else if (selectedColor.equals("Blue")) {
                    changeColour = Color.BLUE;
                } else if (selectedColor.equals("Yellow")) {
                    changeColour = Color.YELLOW;
                }

            }
        });
        subPanel.add(colorComboBox);

        JLabel shapeLabel = new JLabel("Change Shape for Scatter Plot");
        subPanel.add(shapeLabel);
        String[] shapes = {"Circle", "Square", "Triangle"};
        JComboBox<String> shapeComboBox = new JComboBox<String>(shapes);
        shapeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedShape = (String) shapeComboBox.getSelectedItem();

                if (selectedShape.equals("Circle")) {
                    changeShape = new Ellipse2D.Double(-3, -3, 6, 6);
                } else if (selectedShape.equals("Square")) {
                    changeShape = new Rectangle2D.Double(-3, -3, 6, 6);
                } else if (selectedShape.equals("Triangle")) {
                    int[] xPoints = {0, 5, -5}; // X coordinates of the triangle vertices
                    int[] yPoints = {-5, 5, 5}; // Y coordinates of the triangle vertices
                    changeShape = new Polygon(xPoints, yPoints, 3);
                }
            }
        });
        subPanel.add(shapeComboBox);


        settingsPanel.add(subPanel);
        settingsPanel.setVisible(true);

        return settingsPanel;
    }

    public void linechart() throws SQLException {
        linePanel.removeAll();
//        array = LoadDataUI.getRawData();

        Connection connection = ConnectDatabase.getConnection();

        String linequery = "SELECT year, value FROM nhpi WHERE city=? AND province = ?";
        PreparedStatement statement = connection.prepareStatement(linequery);
        statement.setString(1, "Toronto");
        statement.setString(2, "Ontario");
        ResultSet result = statement.executeQuery();

//        String[][] arrayEx = DataQuery.getDataFromDatabase("All", "All", "All",
//                "All", "All", "All");

//        ArrayList<String[]> data = new ArrayList<String[]>();
//        while (result.next()){
//            int year = result.getInt("year");
//            double value = result.getDouble("value");
//            data.add(new String[]{String.valueOf(year), String.valueOf(value)});
//        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("New Housing Price Index");
        dataset.addSeries(series);
        for (String[] x : array) {
            int year = Integer.parseInt(x[0]);
            int month = Integer.parseInt(x[1]);
            double value = Double.parseDouble(x[4]);
            series.addOrUpdate(new Month(month, year), value);
        }

        chart = ChartFactory.createTimeSeriesChart(
                "New Housing Price Index",
                "Year",
                "Price Index",
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        plot.setRangeGridlinesVisible(gridLinesSelected);
        plot.setDomainGridlinesVisible(gridLinesSelected);

        plot.getRenderer().setSeriesPaint(0, changeColour);


//        linePanel = new ChartPanel(chart);
        linePanel.setChart(chart);
        linePanel.setSize(300, 300);

        chartLayout.add(linePanel);
    }

    public void barchart() throws SQLException {
        barPanel.removeAll();
//        String[][] arrayEx = DataQuery.getDataFromDatabase("All", "All", "All",
//                "All", "All", "All");

        Connection connection = ConnectDatabase.getConnection();

        String barquery = "SELECT province, AVG(value) AS averageVal FROM nhpi WHERE year='2020' GROUP BY province";
        PreparedStatement statement = connection.prepareStatement(barquery);
        ResultSet result = statement.executeQuery();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        for (String[] x: arrayEx) {
//            String province = x[2];
//            double averageValue = Double.parseDouble(x[1]);
//            data.addValue(averageValue, "Average NHPI Value", province);
//        }

        Map<String, List<Double>> dataMap = new HashMap<>();
        String province = "";
        for (String[] data : array) {
            province = data[3];
            if (!dataMap.containsKey(province)) {
                dataMap.put(province, new ArrayList<Double>());
            }
            dataMap.get(province).add(Double.valueOf(data[4]));
        }
        for (String key : dataMap.keySet()) {
            double sum = 0.0;
            List<Double> values = dataMap.get(key);
            for (double value : values) {
                sum += value;
            }
            double avg = sum / values.size();
            dataset.addValue(avg, "Average NHPI value", key);
        }


        barchart = ChartFactory.createBarChart(
                "Average NHPI Value by Province",
                "Province", "Average NHPI Value",
                dataset, PlotOrientation.VERTICAL,
                true, true, false);

        CategoryPlot plot = barchart.getCategoryPlot();
        plot.setRangeGridlinesVisible(gridLinesSelected);
        plot.setDomainGridlinesVisible(gridLinesSelected);
        plot.getRenderer().setSeriesPaint(0, changeColour);


        barPanel = new ChartPanel(barchart);

        barPanel.setPreferredSize(new Dimension(400, 400));
        chartLayout.add(barPanel);
    }

    public void scatter() throws SQLException {
        scatterpanel.removeAll();
//        String[][] arrayEx = DataQuery.getDataFromDatabase("All", "All", "All",
//                "All", "All", "All");

        Connection connection = ConnectDatabase.getConnection();

        String scatterquery = "SELECT year, value FROM nhpi WHERE city=? AND province=? AND year BETWEEN ? AND ?";
        PreparedStatement statement = connection.prepareStatement(scatterquery);
        statement.setString(1, "Montreal");
        statement.setString(2, "Quebec");
        statement.setInt(3, 2020);
        statement.setInt(4, 2022);
        ResultSet result = statement.executeQuery();

//        ArrayList<String[]> data = new ArrayList<String[]>();
//        while (result.next()){
//            int year = result.getInt("year");
//            double value = result.getDouble("value");
//            data.add(new String[] { String.valueOf(year), String.valueOf(value) });
//        }

        XYSeriesCollection dataS = new XYSeriesCollection();
        XYSeries series = new XYSeries("New Housing Price Index");
        for (String[] x : array) {
            int year = Integer.parseInt(x[0]);
            double value = Double.parseDouble(x[4]);
            series.add(year, value);
        }
        dataS.addSeries(series);


        scatterchart = ChartFactory.createScatterPlot(
                "Average NHPI Value",
                "Year", "Value",
                dataS, PlotOrientation.VERTICAL,
                true, true, false);

        XYPlot plot = scatterchart.getXYPlot();
        plot.setRangeGridlinesVisible(gridLinesSelected);
        plot.setDomainGridlinesVisible(gridLinesSelected);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShape(0, changeShape);
        plot.setRenderer(renderer);
        plot.getRenderer().setSeriesPaint(0, changeColour);


        scatterpanel = new ChartPanel(scatterchart);
        chartLayout.add(scatterpanel);
    }

    public void piechart() throws SQLException {
        piepanel.removeAll();
//        String[][] arrayEx = DataQuery.getDataFromDatabase("All", "All", "All",
//                "All", "All", "All");
        Connection connection = ConnectDatabase.getConnection();

//        String piequery = "SELECT province, SUM(value) as totalNHPI FROM nhpi GROUP BY province";
//        PreparedStatement statement = connection.prepareStatement(piequery);
//        ResultSet result = statement.executeQuery();
//
//        DefaultPieDataset dataS = new DefaultPieDataset();
//        while (result.next()){
//            String province = result.getString("province");
//            double totalNhpi = result.getDouble("totalNHPI");
//            dataS.setValue(province, totalNhpi);
//        }

        Map<String, Double> provinceSumMap = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            String province = array[i][3];
            double value = Double.parseDouble(array[i][4]);

            if (provinceSumMap.containsKey(province)) {
                double sum = provinceSumMap.get(province);
                sum += value;
                provinceSumMap.put(province, sum);
            } else {
                provinceSumMap.put(province, value);
            }
        }

        // create a pie chart dataset from the provinceSumMap
        DefaultPieDataset dataS = new DefaultPieDataset();
        for (String province : provinceSumMap.keySet()) {
            double sum = provinceSumMap.get(province);
            dataS.setValue(province, sum);
        }

        piechart = ChartFactory.createPieChart(
                "NHPI Values according to Province",
                dataS,
                true,
                true,
                false);

        PiePlot plot = (PiePlot) piechart.getPlot();
        plot.setOutlinePaint(changeColour);
        plot.setOutlineStroke(new BasicStroke(3f));
        piepanel = new ChartPanel(piechart);
        chartLayout.add(piepanel);
    }

    public void valueChanged(ListSelectionEvent e) {
//        try {
////            linechart();
//            barchart();
//            scatter();
//            piechart();
//        } catch (SQLException ex) {
//            throw new RuntimeException(ex);
//        };
//        chartLayout.add(linePanel);
//        chartLayout.add(barPanel);
//        chartLayout.add(scatterpanel);
//        chartLayout.add(piepanel);

        array = LoadDataUI.getRawData();

        linePanel.setVisible(false);
        barPanel.setVisible(false);
        scatterpanel.setVisible(false);
        piepanel.setVisible(false);

        List<String> selectedVisualizations = list.getSelectedValuesList();

        int selectedNumber = list.getSelectedValuesList().size();

        if (selectedNumber>2) {
            JOptionPane.showMessageDialog(MainUI.getInstance(), "Only 2 visualizations can be selected. Please unselect an option before choosing.");
        }
        else{
            for (String x: selectedVisualizations){
                if (x.equals("Line Chart")){
                    try {
                        linechart();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    linePanel.setVisible(true);
                }
                if (x.equals("Bar Chart")){
                    try {
                        barchart();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    barPanel.setVisible(true);

                }
                if (x.equals("Scatter Chart")){
                    try {
                        scatter();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    scatterpanel.setVisible(true);
                }
                if (x.equals("Pie Chart")){
                    try {
                        piechart();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    piepanel.setVisible(true);
                }

            }
        }
    }
}