package statsVisualiser.gui;

import statsVisualiser.DataQuery;
import statsVisualiser.HeaderParameterValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import java.awt.event.ActionListener;

public class UserParametersUI extends JFrame implements ActionListener {

    // Header selection global variables
    JComboBox<String> provincesList;
    JComboBox<String> cityList;
    JComboBox<String> fromYearList;
    JComboBox<String> fromMonthList;
    JComboBox<String> toYearList;
    JComboBox<String> toMonthList;

    // Header button global variables
    JButton addTimeSeriesButton;
    JButton loadDataButton;

    // Selection variables
    Map<String, String> loadDataParams = new HashMap<>();
    ArrayList<HashMap<String, String>> timeSeriesParams = new ArrayList<>();

    // Panel UIs
    JPanel headerSelectionPanel;
    JPanel loadedDataPane;

    // Constructor UIs
    LoadDataUI loadDataUI;

    public UserParametersUI(JPanel loadedDataPane) {
        this.headerSelectionPanel = this.headerSelection();
        this.loadedDataPane = loadedDataPane;
    }

    public JPanel headerSelection() {
        JPanel headerPanel = new JPanel(new BorderLayout());

        JPanel column1HeaderPanel = new JPanel(new FlowLayout());
        JPanel column2HeaderPanel = new JPanel(new FlowLayout());

        JLabel chooseProvince = new JLabel("Province: ");
        Vector<String> provincesNames = HeaderParameterValues.getProvinces();
        JLabel chooseCity = new JLabel("City: ");
        Vector<String> cityNames = HeaderParameterValues.getCities();
        JLabel fromYear = new JLabel("From year: ");
        JLabel fromMonth = new JLabel("From month: ");
        JLabel toYear = new JLabel("To year: ");
        JLabel toMonth = new JLabel("To month: ");
        Vector<String> years = HeaderParameterValues.getYears();
        Vector<String> months = HeaderParameterValues.getMonths();
        provincesList = new JComboBox<String>(provincesNames);
        cityList = new JComboBox<String>(cityNames);
        fromYearList = new JComboBox<String>(years);
        fromMonthList = new JComboBox<String>(months);
        toYearList = new JComboBox<String>(years);
        toMonthList = new JComboBox<String>(months);

        // Adding input options for available parameters
        column1HeaderPanel.add(chooseProvince);
        column1HeaderPanel.add(provincesList);
        column1HeaderPanel.add(chooseCity);
        column1HeaderPanel.add(cityList);
        column1HeaderPanel.add(fromYear);
        column1HeaderPanel.add(fromYearList);
        column1HeaderPanel.add(fromMonth);
        column1HeaderPanel.add(fromMonthList);

        column2HeaderPanel.add(toYear);
        column2HeaderPanel.add(toYearList);
        column2HeaderPanel.add(toMonth);
        column2HeaderPanel.add(toMonthList);

        // Add time series button
        addTimeSeriesButton = new JButton("Add Time Series on Demand");
        column2HeaderPanel.add(addTimeSeriesButton);
        addTimeSeriesButton.addActionListener(this);

        // Add load data button
        loadDataButton = new JButton("Load Data");
        column2HeaderPanel.add(loadDataButton);
        loadDataButton.addActionListener(this);

        headerPanel.add(column1HeaderPanel, BorderLayout.NORTH);
        headerPanel.add(column2HeaderPanel, BorderLayout.SOUTH);

        return headerPanel;
    }

    public JPanel getHeaderSelectionPanel() {
        return this.headerSelectionPanel;
    }

    public void setLoadDataParams(String province, String city, String fromYear,
                                  String fromMonth, String toYear, String toMonth) {
        this.loadDataParams.put("province", province);
        this.loadDataParams.put("city", city);
        this.loadDataParams.put("fromYear", fromYear);
        this.loadDataParams.put("fromMonth", fromMonth);
        this.loadDataParams.put("toYear", toYear);
        this.loadDataParams.put("toMonth", toMonth);
    }

    public void addTimeSeriesParams(String province, String city, String fromYear,
                                    String fromMonth, String toYear, String toMonth) {
        HashMap<String, String> timeSeriesParam = new HashMap<>();
        timeSeriesParam.put("province", province);
        timeSeriesParam.put("city", city);
        timeSeriesParam.put("fromYear", fromYear);
        timeSeriesParam.put("fromMonth", fromMonth);
        timeSeriesParam.put("toYear", toYear);
        timeSeriesParam.put("toMonth", toMonth);
        this.timeSeriesParams.add(timeSeriesParam);
    }

    public void actionPerformed(ActionEvent e) {
        String[][] data = new String[0][];

        if (e.getSource() == addTimeSeriesButton) {
            this.addTimeSeriesParams(provincesList.getSelectedItem().toString(),
                    cityList.getSelectedItem().toString(),
                    fromYearList.getSelectedItem().toString(),
                    fromMonthList.getSelectedItem().toString(),
                    toYearList.getSelectedItem().toString(),
                    toMonthList.getSelectedItem().toString());

            try {
                data = DataQuery.getDataFromDatabase(provincesList.getSelectedItem().toString(),
                        cityList.getSelectedItem().toString(),
                        fromYearList.getSelectedItem().toString(),
                        fromMonthList.getSelectedItem().toString(),
                        toYearList.getSelectedItem().toString(),
                        toMonthList.getSelectedItem().toString());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }

        if (e.getSource() == loadDataButton) {
            this.setLoadDataParams(provincesList.getSelectedItem().toString(),
                    cityList.getSelectedItem().toString(),
                    fromYearList.getSelectedItem().toString(),
                    fromMonthList.getSelectedItem().toString(),
                    toYearList.getSelectedItem().toString(),
                    toMonthList.getSelectedItem().toString());


            try {
                data = DataQuery.getDataFromDatabase(provincesList.getSelectedItem().toString(),
                        cityList.getSelectedItem().toString(),
                        fromYearList.getSelectedItem().toString(),
                        fromMonthList.getSelectedItem().toString(),
                        toYearList.getSelectedItem().toString(),
                        toMonthList.getSelectedItem().toString());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            loadDataUI = new LoadDataUI(data);
            JPanel loadDataPanel = loadDataUI.getLoadDataPanel();

            loadedDataPane.removeAll();
            loadedDataPane.add(loadDataPanel);
            MainUI.getInstance().setVisible(true);
        }

        /*
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
         */
    }
}
