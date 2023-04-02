package statsVisualiser.HeaderParam;

import java.util.Arrays;
import java.util.Vector;

public class ProvincesParam implements HeaderParameter {
    static String[] paramValues = {"All", "Canada", "Atlantic Region", "Newfoundland and Labrador",
            "Prince Edward Island", "Nova Scotia", "New Brunswick", "Quebec",
            "Ontario/Quebec", "Ontario", "Prairie Region", "Manitoba",
            "Saskatchewan", "Alberta", "British Columbia"};

    // Returns unique vector of provinces in the dataset
    @Override
    public Vector<String> getParamValues() {
        Vector<String> provinces = new Vector<String>(Arrays.asList(paramValues));
        return provinces;
    }
}
