package statsVisualiser.gui;

import weka.TimeSeriesForecast;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/*
Class responsibility: [Use case 5] forecast dialog which allows users to specify forecast
parameters (months to forecast, epoch, iterations, and convergence threshold).
*/
public class ForecastDialogUI extends JDialog implements ActionListener {
    static JDialog forecastMenuDialog;
    static JPanel dialogPanel;

    static JComboBox<String> timeSeriesList;
    static private JSpinner monthsSpinner;
    static private JSpinner iterationsSpinner;
    static private JSpinner epochsSpinner;
    static private JSpinner thresholdSpinner;

    static int toYear;
    static int toMonth;
    static int timeSeriesLegendIndex;

    public static void openDialog() {
        if (forecastMenuDialog == null) {
            createDialog();
        }
        forecastMenuDialog.setVisible(true);
    }

    public static void createDialog() {
        Window parentWindow = SwingUtilities.windowForComponent(ForecastSelectionUI.getOpenForecastMenuButton());

        forecastMenuDialog = new JDialog(parentWindow, "Forecasting Parameters");
        forecastMenuDialog.setSize(new Dimension(500, 300));

        createDialogPanel();
        forecastMenuDialog.add(dialogPanel);
        forecastMenuDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        forecastMenuDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    }

    public static void createDialogPanel() {
        dialogPanel = new JPanel(new GridLayout(6, 2));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel timeSeriesLabel = new JLabel("Select Time Series:");
        String[] timeSeries = new String[TimeSeriesUI.getNumOfTimeSeries()];
        for(int timeSeriesIndex=1; timeSeriesIndex<=TimeSeriesUI.getNumOfTimeSeries(); timeSeriesIndex++) {
            timeSeries[timeSeriesIndex - 1] = Integer.toString(timeSeriesIndex);
        }
        Vector<String> timeSeriesSelection = new Vector<String>(Arrays.asList(timeSeries));
        timeSeriesList = new JComboBox<String>(timeSeriesSelection);
        dialogPanel.add(timeSeriesLabel);
        dialogPanel.add(timeSeriesList);

        createLabelForForecastDialogMethod("Number of months to forecast:", dialogPanel);
        monthsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        dialogPanel.add(monthsSpinner);

        createLabelForForecastDialogMethod("Iterations:", dialogPanel);
        iterationsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        dialogPanel.add(iterationsSpinner);

        createLabelForForecastDialogMethod("Epochs:", dialogPanel);
        epochsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        dialogPanel.add(epochsSpinner);

        createLabelForForecastDialogMethod("Convergence Threshold:", dialogPanel);
        thresholdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        dialogPanel.add(thresholdSpinner);

        JButton closeButton = new JButton("Forecast");
        closeButton.addActionListener(new ForecastDialogUI());
        dialogPanel.add(closeButton);
    }

    public static void createLabelForForecastDialogMethod(String labelDescription, JPanel panel){
        JLabel label = new JLabel(labelDescription);
        panel.add(label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        List<Double> forecasts = new ArrayList<>();

        try {
            forecasts = TimeSeriesForecast.getForecasts(getDateToForecastFrom(), getMonths(),
                    getEpochs(), getIterations(), getThreshold());
        } catch (Exception ex) {
            System.out.println("Error");
        }

        ForecastData forecastData = new ForecastData(timeSeriesLegendIndex, toYear, toMonth, forecasts);

        try {
            ForecastUI.showForecast(forecastData);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        forecastMenuDialog.dispose();
    }

    public static String getDateToForecastFrom() {
        timeSeriesLegendIndex = Integer.parseInt(timeSeriesList.getSelectedItem().toString()) - 1;

        HashMap<String, String> param = TimeSeriesUI.getTimeSeriesParams().get(timeSeriesLegendIndex);

        String toDate;
        if(param.get("toYear") == "All" || param.get("toMonth") == "All") {
            toDate = "2022-12-01";
            toYear = 2022;
            toMonth = 12;
        } else {
            toDate = param.get("toYear") + "-" + param.get("toMonth") + "-01";
            toYear = Integer.parseInt(param.get("toYear"));
            toMonth = Integer.parseInt(param.get("toMonth"));
        }
        return toDate;
    }

    public static int getMonths() {
        return (int) monthsSpinner.getValue();
    }

    public static int getIterations() {
        return (int) iterationsSpinner.getValue();
    }

    public static int getEpochs() {
        return (int) epochsSpinner.getValue();
    }

    public static int getThreshold() {
        return (int) thresholdSpinner.getValue();
    }
}
