package statsVisualiser.gui;

import statsVisualiser.DataQuery;
import statsVisualiser.ErrorComponents.ErrorUI;
import statsVisualiser.ErrorComponents.ErrorUserParams;
import statsVisualiser.ErrorComponents.ErrorVisualizations;
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

/*
Class responsibility: [Use case 1] Renders user parameters
input panel where users can specify province, city, and dates
they want from the dataset.
*/
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
            DataQuery query = new DataQuery(provincesList.getSelectedItem().toString(),
                    cityList.getSelectedItem().toString(),
                    fromYearList.getSelectedItem().toString(),
                    fromMonthList.getSelectedItem().toString(),
                    toYearList.getSelectedItem().toString(),
                    toMonthList.getSelectedItem().toString());
            data = DataQuery.getDataFromDatabase(query);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        ErrorUI error = new ErrorUserParams(data);
        if(error.isValid()) {
            UserParametersUI.setLoadDataParams(provincesList.getSelectedItem().toString(),
                    cityList.getSelectedItem().toString(),
                    fromYearList.getSelectedItem().toString(),
                    fromMonthList.getSelectedItem().toString(),
                    toYearList.getSelectedItem().toString(),
                    toMonthList.getSelectedItem().toString());

            loadDataUI = new LoadDataUI(data);
            JPanel loadDataPanelTemp = loadDataUI.getLoadDataPanel();
            loadedDataPanel.removeAll();
            loadedDataPanel.add(loadDataPanelTemp);
            MainUI.getInstance().setVisible(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addTimeSeriesButton) {
            HashMap<String, String> userParams = new HashMap<>();
            userParams.put("province", provincesList.getSelectedItem().toString());
            userParams.put("city", cityList.getSelectedItem().toString());
            userParams.put("fromYear", fromYearList.getSelectedItem().toString());
            userParams.put("fromMonth", fromMonthList.getSelectedItem().toString());
            userParams.put("toYear", toYearList.getSelectedItem().toString());
            userParams.put("toMonth", toMonthList.getSelectedItem().toString());
            TimeSeriesUI.addTimeSeries(userParams);
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

    public static String getUserParams() {
        return "Province: " + UserParametersUI.loadDataParams.get("province") +
                ", City: " + UserParametersUI.loadDataParams.get("city") +
                ", fromYear: " + UserParametersUI.loadDataParams.get("fromYear") +
                ", fromMonth: " + UserParametersUI.loadDataParams.get("fromMonth") +
                ", toYear: " + UserParametersUI.loadDataParams.get("toYear") +
                ", toMonth: " + UserParametersUI.loadDataParams.get("toMonth");
    }
}
