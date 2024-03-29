package statsVisualiser.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;

/*
Class responsibility: [Use case 5] forecast selection UI which appears in the
main application frame allowing user to selection forecast prediction
method (the model) and which loaded time series they would like to
perform forecast predictions on.
*/
public class ForecastSelectionUI implements ActionListener  {
    static JPanel forecastMenuPanel;
    static JButton openForecastMenuButton;

    public static JPanel getForecastMenuPanel() {
        if (forecastMenuPanel == null) {
            createForecastMenuPanel();
        }
        return forecastMenuPanel;
    }

    public static void createForecastMenuPanel() {
        forecastMenuPanel = new JPanel();
        JLabel forecastLabel = new JLabel("Forecast Model: ");
        String[] forecastModelArr = {"Time Series Forecast Package"};
        Vector<String> forecastModel = new Vector<String>(Arrays.asList(forecastModelArr));

        JComboBox<String> forecastModelList = new JComboBox<String>(forecastModel);

        openForecastMenuButton = new JButton("Open Forecast Menu");
        openForecastMenuButton.addActionListener(new ForecastSelectionUI());

        forecastMenuPanel.add(forecastLabel);
        forecastMenuPanel.add(forecastModelList);

        forecastMenuPanel.add(openForecastMenuButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ForecastDialogUI.openDialog();
    }

    public static JButton getOpenForecastMenuButton() {
        return openForecastMenuButton;
    }
}
