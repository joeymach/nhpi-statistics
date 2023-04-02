package statsVisualiser.gui;

import statsVisualiser.DataQuery;
import statsVisualiser.HeaderParam.HeaderParameter;
import statsVisualiser.HeaderParam.HeaderParameterValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import java.awt.event.ActionListener;

public class UserParametersUI extends JFrame implements ActionListener {

    // Header selection global variables
    static JComboBox<String> provincesList;
    static JComboBox<String> cityList;
    static JComboBox<String> fromYearList;
    static JComboBox<String> fromMonthList;
    static JComboBox<String> toYearList;
    static JComboBox<String> toMonthList;

    // Header button global variables
    JButton addTimeSeriesButton;
    JButton loadDataButton;

    // Selection variables
    static Map<String, String> loadDataParams = new HashMap<>();

    // Panel UIs
    JPanel headerSelectionPanel;
    JPanel loadedDataPanel;

    // Constructor UIs
    LoadDataUI loadDataUI;

    public UserParametersUI(JPanel loadedDataPanel) {
        this.headerSelectionPanel = this.headerSelection();
        this.loadedDataPanel = loadedDataPanel;
    }

    public JPanel headerSelection() {
        JPanel headerPanel = new JPanel(new BorderLayout());

        JPanel column1HeaderPanel = new JPanel(new FlowLayout());
        JPanel column2HeaderPanel = new JPanel(new FlowLayout());

        JLabel chooseProvince = new JLabel("Province: ");
        Vector<String> provincesNames = HeaderParameterValues.getParams("provinces");
        JLabel chooseCity = new JLabel("City: ");
        Vector<String> cityNames = HeaderParameterValues.getParams("cities");
        JLabel fromYear = new JLabel("From year: ");
        JLabel fromMonth = new JLabel("From month: ");
        JLabel toYear = new JLabel("To year: ");
        JLabel toMonth = new JLabel("To month: ");
        Vector<String> years = HeaderParameterValues.getParams("years");;
        Vector<String> months = HeaderParameterValues.getParams("month");
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

    public static void setLoadDataParams(String province, String city, String fromYear,
                                  String fromMonth, String toYear, String toMonth) {
        UserParametersUI.loadDataParams.put("province", province);
        UserParametersUI.loadDataParams.put("city", city);
        UserParametersUI.loadDataParams.put("fromYear", fromYear);
        UserParametersUI.loadDataParams.put("fromMonth", fromMonth);
        UserParametersUI.loadDataParams.put("toYear", toYear);
        UserParametersUI.loadDataParams.put("toMonth", toMonth);
    }

    public void loadDataUIToFrame() {
        String[][] data = new String[1][1];
        try {
            data = DataQuery.getDataFromDatabase(loadDataParams.get("province"),
                    loadDataParams.get("city"),
                    loadDataParams.get("fromYear"),
                    loadDataParams.get("fromMonth"),
                    loadDataParams.get("toYear"),
                    loadDataParams.get("toMonth"));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (data.length == 0 || data[0][0].equals("Invalid")) {
            // Error alert window shown when selection parameters are invalid
            JOptionPane.showMessageDialog(MainUI.getInstance(), "Invalid parameters, please choose again.");
        }
        else {
            loadDataUI = new LoadDataUI(data);
            JPanel loadDataPanelTemp = loadDataUI.getLoadDataPanel();
            loadedDataPanel.removeAll();
            loadedDataPanel.add(loadDataPanelTemp);
            MainUI.getInstance().setVisible(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addTimeSeriesButton) {
            TimeSeriesUI.addTimeSeries(provincesList.getSelectedItem().toString(),
                    cityList.getSelectedItem().toString(),
                    fromYearList.getSelectedItem().toString(),
                    fromMonthList.getSelectedItem().toString(),
                    toYearList.getSelectedItem().toString(),
                    toMonthList.getSelectedItem().toString());
        }
        if (e.getSource() == loadDataButton) {
            UserParametersUI.setLoadDataParams(provincesList.getSelectedItem().toString(),
                    cityList.getSelectedItem().toString(),
                    fromYearList.getSelectedItem().toString(),
                    fromMonthList.getSelectedItem().toString(),
                    toYearList.getSelectedItem().toString(),
                    toMonthList.getSelectedItem().toString());

            loadDataUIToFrame();
        }
    }
}
