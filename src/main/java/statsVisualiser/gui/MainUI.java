package statsVisualiser.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Vector;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;

import org.jfree.chart.renderer.xy.XYSplineRenderer;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;

import java.sql.*;

import database.ConnectDatabase;

public class MainUI extends JFrame implements ListSelectionListener {

	private static final long serialVersionUID = 1L;

	static JPanel midContainer = new JPanel();

	// Panels
	JPanel loadedDataPane = new JPanel();
	JPanel visualizationPanel = new JPanel();
	JPanel timeSeriesPanel = new JPanel();

	// UI instance variables
	UserParametersUI userParametersUI;

	// Other time series (to be refactored)
	TimeSeriesCollection initialTimeSeriesDataset = new TimeSeriesCollection();

	ArrayList<String[][]> dataList = new ArrayList<String[][]>();
	ArrayList<String[][]> dataListForTimeSeries = new ArrayList<String[][]>();

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
	ChartPanel piepanel = new ChartPanel(chart);;

	// MainUI instance
	private static MainUI instance;

	public static MainUI getInstance() {
		if (instance == null)
			instance = new MainUI();
		return instance;
	}

	// MainUI frame
	public static JFrame frame = MainUI.getInstance();

	public MainUI() {
		// Set window title
		super("NHPI Statistics");

		// Header selection panel
		this.userParametersUI =  new UserParametersUI(loadedDataPane);
		JPanel headerSelectionPanel = userParametersUI.getHeaderSelectionPanel();
		getContentPane().add(headerSelectionPanel, BorderLayout.NORTH);

		// Setting up wrapper mid-container for the below panels
		midContainer.setLayout(new GridLayout(4, 1));
		JScrollPane mainScrollPane = new JScrollPane(midContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		getContentPane().add(mainScrollPane, BorderLayout.CENTER);

		// 1st panel: raw data table or descriptive stats
		midContainer.add(loadedDataPane, BorderLayout.AFTER_LINE_ENDS);

		// 2nd Panel: Visualizations
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setPreferredSize(new Dimension(200, 200));
		list.addListSelectionListener(this);

		chartLayout.setLayout(new GridLayout(1, 4));
		chartLayout.setPreferredSize(new Dimension(600, 400));
		try {
			linechart();
			barchart();
			scatter();
			piechart();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		};
		chartLayout.add(linePanel);
		chartLayout.add(barPanel);
		chartLayout.add(scatterpanel);
		chartLayout.add(piepanel);
		linePanel.setVisible(false);
		barPanel.setVisible(false);
		scatterpanel.setVisible(false);
		piepanel.setVisible(false);

		visualizationPanel.setLayout(new GridLayout(2, 1));
		visualizationPanel.add(chartLayout);
		visualizationPanel.add(list);

		midContainer.add(visualizationPanel);

		// 3rd Panel: Time series
		XYPlot initialTimeSeriesPlot = new XYPlot();
		JFreeChart initialTimeSeriesChart = new JFreeChart("NHPI % Change Monthly",
				new Font("Serif", java.awt.Font.BOLD, 18), initialTimeSeriesPlot, true);
		ChartPanel chartPanel = new ChartPanel(initialTimeSeriesChart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		timeSeriesPanel.add(chartPanel);

		initialTimeSeriesPlot.setDataset(0, initialTimeSeriesDataset);
		initialTimeSeriesPlot.setRenderer(0, new XYSplineRenderer());
		DateAxis domainAxis = new DateAxis("Year");
		initialTimeSeriesPlot.setDomainAxis(domainAxis);
		initialTimeSeriesPlot.setRangeAxis(new NumberAxis("NHPI % Change Monthly"));

		midContainer.add(timeSeriesPanel, BorderLayout.CENTER);

		midContainer.setSize(300, 300);
		midContainer.setVisible(true);
	}

	public void linechart() throws SQLException {
		Connection connection = ConnectDatabase.getConnection();

		String linequery = "SELECT year, value FROM nhpi WHERE city=? AND province = ?";
		PreparedStatement statement = connection.prepareStatement(linequery);
		statement.setString(1, "Toronto");
		statement.setString(2, "Ontario");
		ResultSet result = statement.executeQuery();


		ArrayList<String[]> data = new ArrayList<String[]>();
		while (result.next()){
			int year = result.getInt("year");
			double value = result.getDouble("value");
			data.add(new String[]{String.valueOf(year), String.valueOf(value)});
		}

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		TimeSeries series = new TimeSeries("New Housing Price Index for Toronto, Ontario");
		dataset.addSeries(series);
		for (String[] x : data) {
			int year = Integer.parseInt(x[0]);
			double value = Double.parseDouble(x[1]);
			series.addOrUpdate(new Year(year), value);
		}

		chart = ChartFactory.createTimeSeriesChart(
				"New Housing Price Index for Toronto, Ontario",
				"Year",
				"Price Index",
				dataset,
				true,
				true,
				false
		);

		linePanel = new ChartPanel(chart);
		linePanel.setSize(300, 300);
	}

	public void barchart() throws SQLException {
		Connection connection = ConnectDatabase.getConnection();

		String barquery = "SELECT province, AVG(value) AS averageVal FROM nhpi WHERE year='2020' GROUP BY province";
		PreparedStatement statement = connection.prepareStatement(barquery);
		ResultSet result = statement.executeQuery();

		DefaultCategoryDataset data = new DefaultCategoryDataset();
		while (result.next()){
			String province = result.getString("province");
			double averageValue = result.getDouble("averageVal");
			data.addValue(averageValue, "Average NHPI Value", province);
		}

		barchart = ChartFactory.createBarChart(
				"Average NHPI Value by Province in 2020",
				"Province", "Average NHPI Value",
				data, PlotOrientation.VERTICAL,
				true, true, false);

		barPanel = new ChartPanel(barchart);
		barPanel.setPreferredSize(new Dimension(400, 400));
	}

	public void scatter() throws SQLException {
		Connection connection = ConnectDatabase.getConnection();

		String scatterquery = "SELECT year, value FROM nhpi WHERE city=? AND province=? AND year BETWEEN ? AND ?";
		PreparedStatement statement = connection.prepareStatement(scatterquery);
		statement.setString(1, "Montreal");
		statement.setString(2, "Quebec");
		statement.setInt(3, 2020);
		statement.setInt(4, 2022);
		ResultSet result = statement.executeQuery();

		ArrayList<String[]> data = new ArrayList<String[]>();
		while (result.next()){
			int year = result.getInt("year");
			double value = result.getDouble("value");
			data.add(new String[] { String.valueOf(year), String.valueOf(value) });
		}

		XYSeriesCollection dataS = new XYSeriesCollection();
		XYSeries series = new XYSeries("New Housing Price Index for Montreal, 2020");
		for (String[] x : data) {
			int year = Integer.parseInt(x[0]);
			double value = Double.parseDouble(x[1]);
			series.add(year, value);
		}
		dataS.addSeries(series);

		scatterchart = ChartFactory.createScatterPlot(
				"Average NHPI Value in Montreal from 2020-2022",
				"Year", "Value",
				dataS, PlotOrientation.VERTICAL,
				true, true, false);

		scatterpanel = new ChartPanel(scatterchart);
	}

	public void piechart() throws SQLException {
		Connection connection = ConnectDatabase.getConnection();

		String piequery = "SELECT province, SUM(value) as totalNHPI FROM nhpi GROUP BY province";
		PreparedStatement statement = connection.prepareStatement(piequery);
		ResultSet result = statement.executeQuery();

		DefaultPieDataset dataS = new DefaultPieDataset();
		while (result.next()){
			String province = result.getString("province");
			double totalNhpi = result.getDouble("totalNHPI");
			dataS.setValue(province, totalNhpi);
		}

		piechart = ChartFactory.createPieChart(
				"NHPI Values according to Province",
				dataS,
				true,
				true,
				false);

		piepanel = new ChartPanel(piechart);
	}

	public void valueChanged(ListSelectionEvent e) {
		linePanel.setVisible(false);
		barPanel.setVisible(false);
		scatterpanel.setVisible(false);
		piepanel.setVisible(false);

		List<String> selectedVisualizations = list.getSelectedValuesList();

		int selectedNumber = list.getSelectedValuesList().size();

		if (selectedNumber>2) {
			JOptionPane.showMessageDialog(list, "Only 2 visualizations can be selected. Please unselect an option before choosing.");
		}
		else{
			for (String x: selectedVisualizations){
				if (x.equals("Line Chart")){
					linePanel.setVisible(true);
				}
				if (x.equals("Bar Chart")){
					barPanel.setVisible(true);

				}
				if (x.equals("Scatter Chart")){
					scatterpanel.setVisible(true);
				}
				if (x.equals("Pie Chart")){
					piepanel.setVisible(true);
				}

			}
		}
	}

	public static void main(String[] args) {
		frame = MainUI.getInstance();
		frame.setSize(1600, 900);
		frame.setVisible(true);
	}
}
