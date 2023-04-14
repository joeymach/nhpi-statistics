package statsVisualiser.gui;

import com.formdev.flatlaf.ui.FlatListCellBorder;
import statsVisualiser.Stats.DescriptiveStats;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
Class responsibility: [Use case 1] show the loaded data table in
either raw data table format, or as a table of descriptive statistics
of the data (avg, median, st.d, min, max).
*/
public class LoadDataUI implements ActionListener {
    // Data variables
    static String[][] rawData;
    String[][] descData;

    // Panel variables
    private JPanel loadDataPanel;
    private JScrollPane rawDataTable;
    private JScrollPane descriptiveTable;

    // Toggle between raw and descriptive table variables
    private JButton toggleButton;
    private boolean showRawData;

    public LoadDataUI(String[][] rawData) {
        this.rawData =  rawData;
        createLoadDataPanel();
    }

    public void createLoadDataPanel() {
        createTableForRawData();
        createTableForDescData();

        loadDataPanel = new JPanel(new BorderLayout());

        toggleButton = new JButton("Toggle to Descriptive Data");
        toggleButton.addActionListener(this);
        showRawData = true;

        loadDataPanel.add(toggleButton, BorderLayout.NORTH);
        loadDataPanel.add(rawDataTable, BorderLayout.CENTER);
    }

    public void createTableForRawData() {
        String[] columnNames = {"Year", "Month", "City", "Province", "NHPI % Change"};
        DefaultTableModel model = new DefaultTableModel(rawData, columnNames);
        rawDataTable = createTableGeneric(model, "Raw Data Table: ");
    }

    public void createTableForDescData() {
        String[] columnNames = {"Data Description", "Value"};
        descData = DescriptiveStats.getDescriptiveStats(rawData);
        DefaultTableModel model = new DefaultTableModel(descData, columnNames);
        descriptiveTable = createTableGeneric(model, "Descriptive Data Table: ");
    }

    private JScrollPane createTableGeneric(DefaultTableModel model, String tableTitle) {
        JTable table = new JTable(model);

        TableColumnModel columnModel = table.getColumnModel();
        if(columnModel.getColumnCount() >= 3) {
            columnModel.getColumn(2).setPreferredWidth(200);
            columnModel.getColumn(3).setPreferredWidth(200);
        }

        String title = tableTitle + UserParametersUI.getUserParams();
        JScrollPane genericTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        genericTable.setPreferredSize(new Dimension(1000, 600));
        genericTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), title,
                TitledBorder.CENTER, TitledBorder.TOP));
        return genericTable;
    }

    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == toggleButton) {
            if(showRawData) {
                // Switch to descriptive data
                loadDataPanel.removeAll();
                loadDataPanel.add(toggleButton, BorderLayout.NORTH);
                loadDataPanel.add(descriptiveTable, BorderLayout.CENTER);
                toggleButton.setText("Toggle to Raw Data");
                showRawData = !showRawData;
                MainUI.getInstance().setVisible(true);
            } else {
                //  Switch to raw data
                loadDataPanel.removeAll();
                loadDataPanel.add(toggleButton, BorderLayout.NORTH);
                loadDataPanel.add(rawDataTable, BorderLayout.CENTER);
                toggleButton.setText("Toggle to Descriptive Data");
                showRawData = !showRawData;
                MainUI.getInstance().setVisible(true);
            }
        }
    }

    public JPanel getLoadDataPanel() {
        return this.loadDataPanel;
    }

    public static String[][] getRawData() {
        return rawData;
    }
}
