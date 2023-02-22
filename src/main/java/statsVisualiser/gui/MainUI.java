/*************************************************
 * FALL 2022
 * EECS 3311 GUI SAMPLE CODE
 * ONLT AS A REFERENCE TO SEE THE USE OF THE jFree FRAMEWORK
 * THE CODE BELOW DOES NOT DEPICT THE DESIGN TO BE FOLLOWED 
 */

package statsVisualiser.gui;

import java.awt.BasicStroke;
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
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.sql.*;

import statsVisualiser.Utils;
import database.ConnectDatabase;

public class MainUI extends JFrame implements ActionListener {
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
	JButton submitButton = new JButton("Submit");
	JButton addTimeSeries = new JButton("Add Time Series");

	ArrayList<String[]> data = new ArrayList<String[]>();

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
		headerSelection(north);
		getContentPane().add(north, BorderLayout.NORTH);

		midContainer.setLayout(new GridLayout(10, 1));
		JScrollPane scrPane = new JScrollPane(midContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		getContentPane().add(scrPane, BorderLayout.CENTER);
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

		north.add(submitButton);
		submitButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
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

		createTableForDataLoading(midContainer, data);

		JPanel west = new JPanel();
		createTimeSeries(west);
		midContainer.add(west, BorderLayout.CENTER);

		frame.setVisible(true);

	}

	private ArrayList<String[]> getDataFromDatabase(String province, String city, String fromYear,
									 String fromMonth, String toYear, String toMonth) throws SQLException {
		ConnectDatabase mysql = new ConnectDatabase();
		Connection connection = mysql.getConnection();

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
		return data;
	}

	private void createTableForDataLoading(JPanel container, ArrayList<String[]> data) {
		String[] columnNames = {"Year", "Month", "City", "Province", "NHPI % Change"};

		String[][] data1 = new String[data.size()][5];
		Arrays.setAll(data1, data::get);

		DefaultTableModel model = new DefaultTableModel(data1, columnNames);

		JTable table = new JTable(model);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane pane = new JScrollPane(table);

		pane.setPreferredSize(new Dimension(600, 400));
		pane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		container.add(pane);
	}

	private void createTimeSeries(JPanel west) {
		TimeSeries series1 = new TimeSeries("Mortality/1000 births");
		series1.add(new Year(2018), 5.6);
		series1.add(new Year(2017), 5.7);
		series1.add(new Year(2016), 5.8);
		series1.add(new Year(2015), 5.8);
		series1.add(new Year(2014), 5.9);
		series1.add(new Year(2013), 6.0);
		series1.add(new Year(2012), 6.1);
		series1.add(new Year(2011), 6.2);
		series1.add(new Year(2010), 6.4);

		TimeSeries series2 = new TimeSeries("Health Expenditure per Capita");
		series2.add(new Year(2018), 10624);
		series2.add(new Year(2017), 10209);
		series2.add(new Year(2016), 9877);
		series2.add(new Year(2015), 9491);
		series2.add(new Year(2014), 9023);
		series2.add(new Year(2013), 8599);
		series2.add(new Year(2012), 8399);
		series2.add(new Year(2011), 8130);
		series2.add(new Year(2010), 7930);
		TimeSeriesCollection dataset2 = new TimeSeriesCollection();
		dataset2.addSeries(series2);

		TimeSeries series3 = new TimeSeries("Hospital Beds/1000 people");
		series3.add(new Year(2018), 2.92);
		series3.add(new Year(2017), 2.87);
		series3.add(new Year(2016), 2.77);
		series3.add(new Year(2015), 2.8);
		series3.add(new Year(2014), 2.83);
		series3.add(new Year(2013), 2.89);
		series3.add(new Year(2012), 2.93);
		series3.add(new Year(2011), 2.97);
		series3.add(new Year(2010), 3.05);

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series3);

		XYPlot plot = new XYPlot();
		XYSplineRenderer splinerenderer1 = new XYSplineRenderer();
		XYSplineRenderer splinerenderer2 = new XYSplineRenderer();

		plot.setDataset(0, dataset);
		plot.setRenderer(0, splinerenderer1);
		DateAxis domainAxis = new DateAxis("Year");
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(new NumberAxis(""));

		plot.setDataset(1, dataset2);
		plot.setRenderer(1, splinerenderer2);
		plot.setRangeAxis(1, new NumberAxis("US$"));

		plot.mapDatasetToRangeAxis(0, 0);// 1st dataset to 1st y-axis
		plot.mapDatasetToRangeAxis(1, 1); // 2nd dataset to 2nd y-axis

		JFreeChart chart = new JFreeChart("Mortality vs Expenses & Hospital Beds",
				new Font("Serif", java.awt.Font.BOLD, 18), plot, true);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(400, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		west.add(chartPanel);

	}

	public static void main(String[] args) {
		frame = MainUI.getInstance();
		frame.setSize(1400, 900);
		frame.setVisible(true);
	}
}