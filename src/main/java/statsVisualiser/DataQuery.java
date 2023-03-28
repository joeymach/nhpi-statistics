package statsVisualiser;

import database.ConnectDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataQuery {
    public static String[][] getDataFromDatabase(String province, String city, String fromYear,
                                          String fromMonth, String toYear, String toMonth) throws SQLException {
        String[][] dataCleaned;
        if(!(fromYear == "All" && toYear == "All" && fromMonth == "All" && toMonth == "All") &&
                (Integer.parseInt(fromYear) > Integer.parseInt(toYear) ||
                        (Integer.parseInt(fromYear) == Integer.parseInt(toYear) &&
                                (Integer.parseInt(fromMonth) > (Integer.parseInt(toMonth)))))) {
            dataCleaned = new String[][]{{"Invalid"}};
            System.out.println(dataCleaned[0][0] + " filter parameters for getting data");
        } else {
            //ConnectDatabase mysql = new ConnectDatabase();
            Connection connection = ConnectDatabase.getConnection();

            String query = ConnectDatabase.getQuery(province, city, fromYear, fromMonth, toYear, toMonth);

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            ArrayList<String[]> data = new ArrayList<String[]>();
            while (result.next())
            {
                data.add(new String[]{result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5)});
            }
            connection.close();

            dataCleaned = new String[data.size()][5];
            Arrays.setAll(dataCleaned, data::get);
        }

        return dataCleaned;
    }
}
