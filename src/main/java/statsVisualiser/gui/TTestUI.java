package statsVisualiser.gui;

import TTest.TTest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class TTestUI implements ActionListener {
    static JComboBox timeSeriesList1;
    static JComboBox timeSeriesList2;
    static JButton submitTTest;
    static JPanel TTestSelection;

    public static void initializeTTestPanel() {
        int timeSeriesNum = TimeSeriesUI.getNumOfTimeSeries();

        JLabel timeSeries1Label = new JLabel("Select Time Series 1:");
        JLabel timeSeries2Label = new JLabel("Select Time Series 2:");
        String[] timeSeries = new String[timeSeriesNum];
        for(int timeSeriesIndex=1; timeSeriesIndex<=timeSeriesNum; timeSeriesIndex++) {
            timeSeries[timeSeriesIndex - 1] = Integer.toString(timeSeriesIndex);
        }
        Vector<String> timeSeriesSelection = new Vector<String>(Arrays.asList(timeSeries));
        timeSeriesList1 = new JComboBox<String>(timeSeriesSelection);
        timeSeriesList2 = new JComboBox<String>(timeSeriesSelection);
        submitTTest = new JButton("Run TTest");
        submitTTest.addActionListener(new TTestUI());

        TTestSelection = new JPanel();
        TTestSelection.add(timeSeries1Label);
        TTestSelection.add(timeSeriesList1);
        TTestSelection.add(timeSeries2Label);
        TTestSelection.add(timeSeriesList2);
        TTestSelection.add(submitTTest);

        MainUI.getTTestPanel().removeAll();
        MainUI.getTTestPanel().add(TTestSelection);
        MainUI.getInstance().setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        ArrayList<HashMap<String, String>> params = TimeSeriesUI.getTimeSeriesParams();

        int timeSeries1Selected = Integer.parseInt(timeSeriesList1.getSelectedItem().toString());
        int timeSeries2Selected = Integer.parseInt(timeSeriesList2.getSelectedItem().toString());

        HashMap<String, String> param1 = params.get(timeSeries1Selected - 1);
        HashMap<String, String> param2 = params.get(timeSeries2Selected - 1);

        String tTestResult = "";
        try {
            tTestResult += TTest.runTTest(param1, param2);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        JPanel TTestUIPanel = new JPanel();
        String jLabelString = "<html>" + "<br>TTest between Time Series #" + timeSeries1Selected +
                " and Time Series #" + timeSeries2Selected +
                "<br>" + tTestResult + "</html>";
        JLabel resultLabel = new JLabel(jLabelString);

        TTestUIPanel.removeAll();
        TTestUIPanel.add(TTestSelection);
        TTestUIPanel.add(resultLabel);

        MainUI.getTTestPanel().removeAll();
        MainUI.getTTestPanel().add(TTestUIPanel);
        MainUI.getInstance().setVisible(true);
    }
}
