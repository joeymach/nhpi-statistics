package statsVisualiser.gui;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;
import org.jfree.chart.JFreeChart;


public class MainUI extends JFrame{

	private static final long serialVersionUID = 1L;

	// midcontainer JPanel
	static JPanel midContainer = new JPanel();

	// Panels
	JPanel loadedDataPane = new JPanel();
	static JPanel visualizationPanel = new JPanel();
	static JPanel settingsPanel = new JPanel();
	static JPanel timeSeriesPanel = new JPanel();
	static JPanel forecastPanel = new JPanel();
	static JPanel tTestPanel = new JPanel();

	// UI instance variables
	UserParametersUI userParametersUI;
	Visualizations visualizationsClass;

    // MainUI instance
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
	static JButton configureButton = new JButton("Configure...");


	public MainUI() {
		// Set window title
		super("NHPI Statistics");
		this.visualizationsClass =  new Visualizations();


		settingsPanel = visualizationsClass.getSettingsPanel();
		settingsPanel.setVisible(false);

//		configureButton.addActionListener(new ActionListener() {
//			private boolean isSettingsPanelVisible = false;
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				isSettingsPanelVisible = !isSettingsPanelVisible; // toggle the visibility flag
//				settingsPanel.setVisible(isSettingsPanelVisible);
//
//			}
//		});
		SettingsActionListener();
		midContainerAdd();


//		// Setting up wrapper mid-container for the below panels
//		JScrollPane mainScrollPane = new JScrollPane(midContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		getContentPane().add(mainScrollPane, BorderLayout.CENTER);
//
//		// Header selection panel
//		this.userParametersUI =  new UserParametersUI(loadedDataPane);
//		JPanel headerSelectionPanel = userParametersUI.getHeaderSelectionPanel();
//		getContentPane().add(headerSelectionPanel, BorderLayout.NORTH);
//
//		// 1st panel: raw data table or descriptive stats
//		midContainer.add(loadedDataPane);
//
//		// 2nd Panel: Time series
//		midContainer.add(timeSeriesPanel);
//
//		// 3rd Panel: TTest
//		midContainer.add(tTestPanel);
//
//		// 4th Panel: Forecast
//		midContainer.add(forecastPanel);
//
//		// 5th panel: Visualizations use case 3
//		visualizationPanel = this.visualizationsClass.getVisualizations();
//		midContainer.add(visualizationPanel);
//		midContainer.add(configureButton);
//		midContainer.add(settingsPanel);
//
//		midContainer.setLayout(new BoxLayout(midContainer, BoxLayout.Y_AXIS));
//		midContainer.setVisible(true);
	}

	public static void SettingsActionListener(){
		configureButton.addActionListener(new ActionListener() {
			private boolean isSettingsPanelVisible = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				isSettingsPanelVisible = !isSettingsPanelVisible; // toggle the visibility flag
				settingsPanel.setVisible(isSettingsPanelVisible);

			}
		});
	}

	public void midContainerAdd(){
		// Setting up wrapper mid-container for the below panels
		JScrollPane mainScrollPane = new JScrollPane(midContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		getContentPane().add(mainScrollPane, BorderLayout.CENTER);

		// Header selection panel
		this.userParametersUI =  new UserParametersUI(loadedDataPane);
		JPanel headerSelectionPanel = userParametersUI.getHeaderSelectionPanel();
		getContentPane().add(headerSelectionPanel, BorderLayout.NORTH);

		// 1st panel: raw data table or descriptive stats
		midContainer.add(loadedDataPane);

		// 2nd Panel: Time series
		midContainer.add(timeSeriesPanel);

		// 3rd Panel: TTest
		midContainer.add(tTestPanel);

		// 4th Panel: Forecast
		midContainer.add(forecastPanel);

		// 5th panel: Visualizations use case 3
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