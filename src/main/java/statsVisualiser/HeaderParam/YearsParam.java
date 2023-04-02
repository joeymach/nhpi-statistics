package statsVisualiser.HeaderParam;

import java.util.Vector;

public class YearsParam implements HeaderParameter {

    // Returns vector of Strings from 1981 to 2022 that represents unique years in the dataset
    public Vector<String> getParamValues() {
        Vector<String> years = new Vector<String>();
        years.add("All");
        for (int i = 2022; i >= 1981; i--) {
            years.add("" + i);
        }
        return years;
    }
}
