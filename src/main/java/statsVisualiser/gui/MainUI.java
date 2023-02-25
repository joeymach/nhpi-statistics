package statsVisualiser.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.util.*;
import java.awt.FlowLayout;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.sql.*;

import statsVisualiser.Utils;
import database.ConnectDatabase;

public class MainUI extends JFrame implements ActionListener, ListSelectionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JComboBox<String> provincesList;
	JComboBox<String> cityList;
	JComboBox<String> fromYearList;
	JComboBox<String> fromMonthList;
	JComboBox<String> toYearList;
	JComboBox<String> toMonthList;
	static JPanel midContainer = new JPanel();
	JPanel chartLayout = new JPanel();


	JButton loadData = new JButton("Load Data");
	JButton addTimeSeriesButton = new JButton("Add Time Series");

	JButton button = new JButton("Button");

	String visualizations[] = {"Line Chart", "Bar Chart", "Scatter Chart", "Pie Chart"};
	JList list = new JList(visualizations);
	JFrame lineframe = new JFrame();

	JFreeChart barchart;
	ChartFrame barframe;

	private HashMap<String, JFrame> visualizationFrames;
	private JPanel visualizationFrame;
	JFreeChart chart;
	ChartPanel linePanel = new ChartPanel(chart);
	JPanel barPanel = new ChartPanel(chart);

	JPanel lineMidContainer = new JPanel();
	JFreeChart scatterchart;

	JFrame scatterframe = new JFrame("Scatter Plot");
	ChartPanel scatterpanel = new ChartPanel(chart);

	JPanel scatterMidContainer = new JPanel();

	JFreeChart piechart;
	JFrame pieframe = new JFrame("Pie Chart");
	ChartPanel piepanel = new ChartPanel(chart);;

	int selected = 0;

	XYPlot initialTimeSeriesPlot = new XYPlot();
	JFreeChart initialTimeSeriesChart = new JFreeChart("NHPI % Change Monthly",
			new Font("Serif", java.awt.Font.BOLD, 18), initialTimeSeriesPlot, true);
	JPanel initialTimeSeriesPanel = new JPanel();
	TimeSeriesCollection initialTimeSeriesDataset = new TimeSeriesCollection();

	ArrayList<String[][]> dataList = new ArrayList<String[][]>();
	ArrayList<String[][]> dataListForTimeSeries = new ArrayList<String[][]>();

	private static MainUI instance;

	public static MainUI getInstance() {
		if (instance == null)
			instance = new MainUI();

		return instance;
	}

	public static JFrame frame = MainUI.getInstance();

	private MainUI() {
		// Set window title
		super("NHPI Statistics");

		// Set bottom bar
		JButton recalculate = new JButton("Recalculate");

		JLabel viewsLabel = new JLabel("Available Views: ");

		Vector<String> viewsNames = new Vector<String>();
		viewsNames.add("Pie Chart");
		viewsNames.add("Line Chart");
		viewsNames.add("Bar Chart");
		viewsNames.add("Scatter Chart");
		viewsNames.add("Report");
		JComboBox<String> viewsList = new JComboBox<String>(viewsNames);
		JButton addView = new JButton("+");
		JButton removeView = new JButton("-");

		JLabel methodLabel = new JLabel("        Choose analysis method: ");

		Vector<String> methodsNames = new Vector<String>();
		methodsNames.add("Mortality");
		methodsNames.add("Mortality vs Expenses");
		methodsNames.add("Mortality vs Expenses & Hospital Beds");
		methodsNames.add("Mortality vs GDP");
		methodsNames.add("Unemployment vs GDP");
		methodsNames.add("Unemployment");

		JComboBox<String> methodsList = new JComboBox<String>(methodsNames);

		JPanel north = new JPanel();
		north.setLayout(new GridLayout(2, 1));
		headerSelection(north);
		getContentPane().add(north, BorderLayout.NORTH);

		midContainer.setLayout(new GridLayout(10, 1));
		JScrollPane scrPane = new JScrollPane(midContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		getContentPane().add(scrPane, BorderLayout.CENTER);


		ChartPanel chartPanel = new ChartPanel(initialTimeSeriesChart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		initialTimeSeriesPanel.add(chartPanel);
		initialTimeSeriesPlot.setDataset(0, initialTimeSeriesDataset);
		initialTimeSeriesPlot.setRenderer(0, new XYSplineRenderer());
		DateAxis domainAxis = new DateAxis("Year");
		initialTimeSeriesPlot.setDomainAxis(domainAxis);
		initialTimeSeriesPlot.setRangeAxis(new NumberAxis("NHPI % Change Monthly"));

		midContainer.add(initialTimeSeriesPanel, BorderLayout.CENTER);

		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
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

		midContainer.add(chartLayout);

		visualizationFrames = new HashMap<String, JFrame>();
		visualizationFrame = new JPanel();
		visualizationFrame.setPreferredSize(new Dimension(600, 400));

		midContainer.add(new JScrollPane(list));
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

	public void headerSelection(JPanel north) {
		JLabel chooseProvince = new JLabel("Province: ");
		Vector<String> provincesNames = Utils.getProvinces();
		provincesList = new JComboBox<String>(provincesNames);

		JLabel chooseCity = new JLabel("City: ");
		Vector<String> cityNames = Utils.getCities();
		cityList = new JComboBox<String>(cityNames);

		JLabel fromYear = new JLabel("From year: ");
		JLabel fromMonth = new JLabel("From month: ");
		JLabel toYear = new JLabel("To year: ");
		JLabel toMonth = new JLabel("To month: ");
		Vector<String> years = Utils.getYears();
		Vector<String> months = Utils.getMonths();
		fromYearList = new JComboBox<String>(years);
		fromMonthList = new JComboBox<String>(months);
		toYearList = new JComboBox<String>(years);
		toMonthList = new JComboBox<String>(months);

		north.add(chooseProvince);
		north.add(provincesList);
		north.add(chooseCity);
		north.add(cityList);
		north.add(fromYear);
		north.add(fromYearList);
		north.add(fromMonth);
		north.add(fromMonthList);
		north.add(toYear);
		north.add(toYearList);
		north.add(toMonth);
		north.add(toMonthList);
		north.add(loadData);
		loadData.addActionListener(this);
		north.add(addTimeSeriesButton);
		addTimeSeriesButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		String[][] data = new String[0][];
		try {
			data = getDataFromDatabase(provincesList.getSelectedItem().toString(),
					cityList.getSelectedItem().toString(),
					fromYearList.getSelectedItem().toString(),
					fromMonthList.getSelectedItem().toString(),
					toYearList.getSelectedItem().toString(),
					toMonthList.getSelectedItem().toString());
		} catch(Exception exception){
			exception.printStackTrace();
		}
		dataList.add(data);
		dataListForTimeSeries.add(data);

		if (e.getSource() == loadData) {
			createTableForDataLoading(midContainer, data);
			frame.setVisible(true);
		}

		if (e.getSource() == addTimeSeriesButton) {
			createTimeSeriesForData();
			frame.setVisible(true);
		}
	}

	private String[][] getDataFromDatabase(String province, String city, String fromYear,
										   String fromMonth, String toYear, String toMonth) throws SQLException {
		//ConnectDatabase mysql = new ConnectDatabase();
		Connection connection = ConnectDatabase.getConnection();

		String query = ConnectDatabase.getQuery(province, city, fromYear, fromMonth, toYear, toMonth);

		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet result = statement.executeQuery();

		ArrayList<String[]> data = new ArrayList<String[]>();
		while (result.next())
		{
			data.add(new String[]{result.getString(1),
					result.getString(2),
					result.getString(3),
					result.getString(4),
					result.getString(5)});
		}
		connection.close();

		String[][] dataCleaned = new String[data.size()][5];
		Arrays.setAll(dataCleaned, data::get);

		return dataCleaned;
	}

	private void createTableForDataLoading(JPanel container, String[][] data) {
		String[] columnNames = {"Year", "Month", "City", "Province", "NHPI % Change"};

		DefaultTableModel model = new DefaultTableModel(data, columnNames);

		JTable table = new JTable(model);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane pane = new JScrollPane(table);

		pane.setPreferredSize(new Dimension(600, 400));
		pane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		container.add(pane, BorderLayout.AFTER_LINE_ENDS);
	}

	private void createTimeSeriesForData() {
		for(String[][] dataset : dataListForTimeSeries) {
			TimeSeries series = new TimeSeries("");
			for(String[] row : dataset) {
				series.addOrUpdate(new Year(Integer.parseInt(row[0])), Double.parseDouble(row[4]));
			}
			initialTimeSeriesDataset.addSeries(series);
		}
		dataListForTimeSeries = new ArrayList<String[][]>();
	}



	public static void main(String[] args) {
		frame = MainUI.getInstance();
		frame.setSize(1600, 900);
		frame.setVisible(true);
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
				System.out.println(x.equals("Scatter Chart"));
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
}
