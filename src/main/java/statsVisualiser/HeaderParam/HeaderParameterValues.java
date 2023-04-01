package statsVisualiser.HeaderParam;

import java.util.Vector;

public class HeaderParameterValues {
    public static Vector<String> getParams(String headerParamType) {
        if (headerParamType.equalsIgnoreCase("cities")) {
            return new CitiesParam().getParamValues();
        } else if (headerParamType.equalsIgnoreCase("provinces")) {
            return new ProvincesParam().getParamValues();
        } else if (headerParamType.equalsIgnoreCase("years")) {
            return new YearsParam().getParamValues();
        } else {
            // headerParamType.equalsIgnoreCase("month")
            return new MonthParam().getParamValues();
        }
    }
}
