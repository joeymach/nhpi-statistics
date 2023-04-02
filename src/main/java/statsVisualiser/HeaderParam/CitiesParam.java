package statsVisualiser.HeaderParam;

import java.util.Arrays;
import java.util.Vector;

public class CitiesParam implements HeaderParameter {
    static String[] paramValues = {"All", "St. John's", "Charlottetown", "Halifax",
            "Saint John, Fredericton, and Moncton", "Québec", "Sherbrooke", "Trois-Rivières", "Montréal",
            "Ottawa-Gatineau, Quebec part", "Ottawa-Gatineau, Ontario part", "Oshawa", "Toronto",
            "Hamilton", "St. Catharines-Niagara", "Kitchener-Cambridge-Waterloo", "Guelph",
            "London", "Windsor", "Greater Sudbury", "Winnipeg",
            "Regina", "Saskatoon", "Calgary", "Edmonton",
            "Kelowna", "Vancouver", "Victoria"};

    // Returns unique vector of cities in the dataset
    public Vector<String> getParamValues() {
        Vector<String> citiesNames = new Vector<String>(Arrays.asList(paramValues));
        return citiesNames;
    }
}
