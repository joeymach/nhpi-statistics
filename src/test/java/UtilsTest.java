import static org.junit.Assert.*;

import org.junit.Test;
import statsVisualiser.HeaderParameterValues;

import java.util.Arrays;
import java.util.Vector;

public class UtilsTest {

    @Test
    public void getCitiesTest() {
        System.out.println("Test if getCities returns appropriate unique list of cities");

        String[] citiesArray = {"All", "St. John's", "Charlottetown", "Halifax",
                "Saint John, Fredericton, and Moncton", "Québec", "Sherbrooke", "Trois-Rivières", "Montréal",
                "Ottawa-Gatineau, Quebec part", "Ottawa-Gatineau, Ontario part","Oshawa", "Toronto",
                "Hamilton", "St. Catharines-Niagara","Kitchener-Cambridge-Waterloo", "Guelph",
                "London", "Windsor","Greater Sudbury", "Winnipeg",
                "Regina", "Saskatoon","Calgary", "Edmonton",
                "Kelowna", "Vancouver","Victoria"};

        Vector<String> citiesNamesExpected = new Vector<String>(Arrays.asList(citiesArray));

        Vector<String> citiesNames = HeaderParameterValues.getCities();

        for(int i = 0; i < citiesNamesExpected.size(); i++) {
            assertEquals(citiesNames.get(i), citiesNamesExpected.get(i));
        }
    }

    @Test
    public void getProvincesTest() {
        System.out.println("Test if getProvinces returns appropriate unique list of provinces");

        String[] provincesArray = {"All", "Canada", "Atlantic Region", "Newfoundland and Labrador",
                "Prince Edward Island", "Nova Scotia", "New Brunswick", "Quebec",
                "Ontario/Quebec", "Ontario", "Prairie Region", "Manitoba",
                "Saskatchewan", "Alberta", "British Columbia"};

        Vector<String> provincesExpected = new Vector<String>(Arrays.asList(provincesArray));

        Vector<String> provinces = HeaderParameterValues.getProvinces();

        for(int i = 0; i < provincesExpected.size(); i++) {
            assertEquals(provinces.get(i), provincesExpected.get(i));
        }
    }
}
