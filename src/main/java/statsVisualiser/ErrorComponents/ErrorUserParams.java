package statsVisualiser.ErrorComponents;

import statsVisualiser.gui.MainUI;

import javax.swing.*;

public class ErrorUserParams implements ErrorUI {
    String[][] data;
    public ErrorUserParams(String[][] data) {
        this.data = data;
    }

    @Override
    public boolean isValid() {
        if (data.length == 0 || data[0][0].equals("Invalid")) {
            displayError();
            return false;
        }
        return true;
    }

    @Override
    public void displayError() {
        // Error alert window shown when selection parameters are invalid
        JOptionPane.showMessageDialog(MainUI.getInstance(), "Invalid parameters, please choose again.");
    }
}
