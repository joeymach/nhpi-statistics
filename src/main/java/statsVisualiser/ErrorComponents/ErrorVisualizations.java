package statsVisualiser.ErrorComponents;

import statsVisualiser.gui.MainUI;

import javax.swing.*;

public class ErrorVisualizations implements ErrorUI {
    int selectedVisualizations;
    public ErrorVisualizations(int selectedVisualizations) {
        this.selectedVisualizations = selectedVisualizations;
    }

    @Override
    public boolean isValid() {
        if (selectedVisualizations > 2) {
            displayError();
            return false;
        }
        return true;
    }

    @Override
    public void displayError() {
        // Error alert window shown when more than 2 visualizations are selected
        JOptionPane.showMessageDialog(MainUI.getInstance(), "Only 2 visualizations can be selected. Please unselect an option before choosing.");
    }
}
