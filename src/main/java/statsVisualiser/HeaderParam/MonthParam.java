package statsVisualiser.HeaderParam;

import java.util.Vector;

public class MonthParam implements HeaderParameter {
    public Vector<String> getParamValues() {
        Vector<String> months = new Vector<String>();
        months.add("All");
        for (int i = 1; i <= 12; i++) {
            months.add("" + i);
        }
        return months;
    }
}
