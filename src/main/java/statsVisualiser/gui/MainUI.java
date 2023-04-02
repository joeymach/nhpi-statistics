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

public class MainUI extends JFrame{

	private static final long serialVersionUID = 1L;

	//midcontainer JPanel
	static JPanel midContainer = new JPanel();
	//
//	// Panels
	JPanel loadedDataPane = new JPanel();
	static JPanel visualizationPanel = new JPanel();
	static JPanel settingsPanel = new JPanel();
	static JPanel timeSeriesPanel = new JPanel();
	static JPanel forecastPanel = new JPanel();
	static JPanel tTestPanel = new JPanel();

	//
//	// UI instance variables
	UserParametersUI userParametersUI;
	Visualizations visualizationsClass;

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

//	 MainUI instance
	private static MainUI instance;
	public static JFrame frame;

	public static MainUI getInstance() {
		if (instance == null)
			instance = new MainUI();
		return instance;
	}

	public static JPanel getTimeSeriesPanel() {
		return timeSeriesPanel;
	}

	public static JPanel getForecastPanel() {
		return forecastPanel;
	}

	public static JPanel getTTestPanel() {
		return tTestPanel;
	}

	public static JPanel getVisualizationPanel() {
		return visualizationPanel;
	}

	public MainUI() {
		// Set window title
		super("NHPI Statistics");
		this.visualizationsClass =  new Visualizations();

		JButton configureButton = new JButton("Configure...");

		JPanel settingsPanel = visualizationsClass.getSettingsPanel();
		settingsPanel.setVisible(false);

		configureButton.addActionListener(new ActionListener() {
			private boolean isSettingsPanelVisible = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				isSettingsPanelVisible = !isSettingsPanelVisible; // toggle the visibility flag
				settingsPanel.setVisible(isSettingsPanelVisible);

			}
		});

		// Header selection panel
		this.userParametersUI =  new UserParametersUI(loadedDataPane);
		JPanel headerSelectionPanel = userParametersUI.getHeaderSelectionPanel();
		getContentPane().add(headerSelectionPanel, BorderLayout.NORTH);

//		JPanel settingsPanel = this.visualizations.getSettingsPanel();
//		midContainer.add(settingsPanel);

		// Setting up wrapper mid-container for the below panels
		//midContainer.setLayout(new BorderLayout());
		JScrollPane mainScrollPane = new JScrollPane(midContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		getContentPane().add(mainScrollPane, BorderLayout.CENTER);

		// 1st panel: raw data table or descriptive stats
		midContainer.add(loadedDataPane);

		// 2nd Panel: Time series
		midContainer.add(timeSeriesPanel);
		midContainer.add(tTestPanel);


		// 3rd Panel: Forecast
		midContainer.add(forecastPanel);


		visualizationPanel = this.visualizationsClass.getVisualizations();
		midContainer.add(visualizationPanel);
		midContainer.add(configureButton);
		midContainer.add(settingsPanel);
		midContainer.setLayout(new BoxLayout(midContainer, BoxLayout.Y_AXIS));
		midContainer.setVisible(true);
	}


	public static void main(String[] args) {
		frame = MainUI.getInstance();
		frame.setSize(1600, 900);
		frame.setVisible(true);
	}
}