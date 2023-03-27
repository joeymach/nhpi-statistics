package statsVisualiser.gui;

import statsVisualiser.DescriptiveStats;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadDataUI implements ActionListener {
    // Data variables
    private String[][] rawData;
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

        JTable table = new JTable(model);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(200);

        rawDataTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        rawDataTable.setPreferredSize(new Dimension(1000, 600));
        rawDataTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Raw Loaded Data Table with province:all, city:all, toyear=1203, tomonth=123, fromYear=2342, frommonth=123123",
                TitledBorder.CENTER, TitledBorder.TOP));
    }

    public void createTableForDescData() {
        String[] columnNames = {"Data Description", "Value"};

        descData = DescriptiveStats.getDescriptiveStats(rawData);
        DefaultTableModel model = new DefaultTableModel(descData, columnNames);

        JTable table = new JTable(model);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        descriptiveTable = new JScrollPane(table);

        descriptiveTable.setPreferredSize(new Dimension(1000, 600));
        descriptiveTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Desc Loaded Data Table with province:all, city:all, toyear=1203, tomonth=123, fromYear=2342, frommonth=123123",
                TitledBorder.CENTER, TitledBorder.TOP));
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
}
