package statsVisualiser;

import java.util.Arrays;
import java.util.Vector;

public class Utils {
    public static Vector<String> getCities() {
        String[] citiesArray = {"All", "St. John's", "Charlottetown", "Halifax",
                "Saint John, Fredericton, and Moncton", "Québec", "Sherbrooke", "Trois-Rivières", "Montréal",
                "Ottawa-Gatineau, Quebec part", "Ottawa-Gatineau, Ontario part","Oshawa", "Toronto",
                "Hamilton", "St. Catharines-Niagara","Kitchener-Cambridge-Waterloo", "Guelph",
                "London", "Windsor","Greater Sudbury", "Winnipeg",
                "Regina", "Saskatoon","Calgary", "Edmonton",
                "Kelowna", "Vancouver","Victoria"};

        Vector<String> citiesNames = new Vector<String>(Arrays.asList(citiesArray));
        return citiesNames;
    }

    public static Vector<String> getProvinces() {
        String[] provincesArray = {"All", "Canada", "Atlantic Region", "Newfoundland and Labrador",
                "Prince Edward Island", "Nova Scotia", "New Brunswick", "Quebec",
                "Ontario/Quebec", "Ontario", "Prairie Region", "Manitoba",
                "Saskatchewan", "Alberta", "British Columbia"};

        Vector<String> provincesNames = new Vector<String>(Arrays.asList(provincesArray));
        return provincesNames;
    }

    public static Vector<String> getYears() {
        Vector<String> years = new Vector<String>();
        years.add("All");
        for (int i = 2022; i >= 1981; i--) {
            years.add("" + i);
        }
        return years;
    }

    public static Vector<String> getMonths() {
        Vector<String> months = new Vector<String>();
        months.add("All");
        for (int i = 1; i <= 12; i++) {
            months.add("" + i);
        }
        return months;
    }
}
