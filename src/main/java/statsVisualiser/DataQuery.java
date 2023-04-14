package statsVisualiser;

import database.ConnectDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataQuery {

    private String province;
    private String city;
    private String fromYear;
    private String fromMonth;
    private String toYear;
    private String toMonth;

    public DataQuery(String province, String city, String fromYear, String fromMonth, String toYear, String toMonth) {
        this.province = province;
        this.city = city;
        this.fromYear = fromYear;
        this.fromMonth = fromMonth;
        this.toYear = toYear;
        this.toMonth = toMonth;
    }

//    public static String[][] getDataFromDatabase(String province, String city, String fromYear,
//                                          String fromMonth, String toYear, String toMonth) throws SQLException {
//        String[][] dataCleaned;
//        if(!(fromYear == "All" && toYear == "All" && fromMonth == "All" && toMonth == "All") &&
//                (Integer.parseInt(fromYear) > Integer.parseInt(toYear) ||
//                        (Integer.parseInt(fromYear) == Integer.parseInt(toYear) &&
//                                (Integer.parseInt(fromMonth) > (Integer.parseInt(toMonth)))))) {
//            dataCleaned = new String[][]{{"Invalid"}};
//            System.out.println(dataCleaned[0][0] + " filter parameters for getting data");
//        } else {
//            //ConnectDatabase mysql = new ConnectDatabase();
//            Connection connection = ConnectDatabase.getConnection();
//
//            String query = ConnectDatabase.getQuery(province, city, fromYear, fromMonth, toYear, toMonth);
//
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet result = statement.executeQuery();
//
//            ArrayList<String[]> data = new ArrayList<String[]>();
//            while (result.next())
//            {
//                data.add(new String[]{result.getString(1),
//                        result.getString(2),
//                        result.getString(3),
//                        result.getString(4),
//                        result.getString(5)});
//            }
//            connection.close();
//
//            dataCleaned = new String[data.size()][5];
//            Arrays.setAll(dataCleaned, data::get);
//        }
//
//        return dataCleaned;
//    }


        public String getFromYear() {
            return fromYear;
        }

        public String getToYear() {
            return toYear;
        }

        public String getFromMonth() {
            return fromMonth;

        }

        public String getToMonth() {
            return toMonth;

        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        // Constructor, getters and setters


    public static String[][] getDataFromDatabase(DataQuery parameters) throws SQLException {
        String[][] dataCleaned;
        if(!(parameters.getFromYear().equals("All") && parameters.getToYear().equals("All")
                && parameters.getFromMonth().equals("All") && parameters.getToMonth().equals("All")) &&
                (Integer.parseInt(parameters.getFromYear()) > Integer.parseInt(parameters.getToYear()) ||
                        (Integer.parseInt(parameters.getFromYear()) == Integer.parseInt(parameters.getToYear()) &&
                                (Integer.parseInt(parameters.getFromMonth()) > (Integer.parseInt(parameters.getToMonth())))))) {
            dataCleaned = new String[][]{{"Invalid"}};
            System.out.println(dataCleaned[0][0] + " filter parameters for getting data");
        } else {
            Connection connection = ConnectDatabase.getConnection();

            String query = ConnectDatabase.getQuery(parameters.getProvince(), parameters.getCity(),
                    parameters.getFromYear(), parameters.getFromMonth(), parameters.getToYear(), parameters.getToMonth());

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
