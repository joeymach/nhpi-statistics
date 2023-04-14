package database;

import java.io.*;
import java.sql.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class ConnectDatabase {
    public static Connection connection = null;
    static int queryCount = 0;

    public void establishConnection() throws Exception {
        int batchSize=20;
        String filePath="nhpi.csv";

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Setup the connection with the DB
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost/nhpi?"
                            + "user=root&password=");

            connection.setAutoCommit(false);

            String sql="insert into nhpi(year,month,city,province,value) values(?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));

            String lineText=null;
            int count=0;

            lineReader.readLine();
            while ((lineText=lineReader.readLine())!=null){

                int i = lineText.lastIndexOf(",");
                String[] data =  {lineText.substring(0, i), lineText.substring(i + 1)};
                String[] dateAndLocation = data[0].split(",", 2);

                int year = parseInt(dateAndLocation[0].split("-")[0]);
                int month = parseInt(dateAndLocation[0].split("-")[1]);

                int x = dateAndLocation[1].lastIndexOf(",");
                String city, province;
                if(x >= 0) {
                    String[] location =  {dateAndLocation[1].substring(0, x), dateAndLocation[1].substring(x + 2)};
                    city = location[0].replaceAll("\"", "");
                    province = location[1].replaceAll("\"", "");
                } else {
                    city = "";
                    province = dateAndLocation[1];
                }

                Double value = 0.00;
                try{
                    value = parseDouble(data[data.length-1]);
                } catch(Exception e) {
                }

                statement.setInt(1, year);
                statement.setInt(2, month);
                statement.setString(3, city);
                statement.setString(4, province);
                statement.setDouble(5, value);

                statement.addBatch();

                if(count%batchSize==0){
                    statement.executeBatch();
                }
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("Data has been inserted successfully.");
        }
        catch (Exception exception){
            exception.printStackTrace();
        }

    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // singleton design pattern implementation
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost/nhpi?"
                            + "user=root&password=");

            connection.setAutoCommit(false);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        return connection;
    }

    public static String getQuery(String province, String city, String fromYear,
                           String fromMonth, String toYear, String toMonth) {
        String query = "SELECT * from nhpi";
        if (province != "All" || city != "All" ||
                fromYear != "All" || fromMonth != "All" ||
                toYear != "All" || toMonth != "All") {
            query += " WHERE";
        }

        query += addAndClause(queryCount);
        query += checkProvince(province);
        query += addAndClause(queryCount);
        query += checkCity(city);
        query += addAndClause(queryCount);
        query += checkFromYearAndMonth(fromYear, fromMonth);
        query += addAndClause(queryCount);
        query += checkToYearAndMonth(toYear, toMonth);

        query += ";";
        return query;
    }

    public static String checkProvince(String province){
        if(province != "All"){
            ConnectDatabase.queryCount += 1;
            String clause = " province=\"" + province + "\"";
            return clause;
        }
        return "";
    }

    public static String checkCity(String city){
        if(city != "All"){
            ConnectDatabase.queryCount += 1;
            String clause = " city=\"" + city + "\"";
            return clause;
        }
        return "";
    }

    public static String checkFromYearAndMonth(String fromYear, String fromMonth){
        if(fromYear != "All" && fromMonth != "All") {
            ConnectDatabase.queryCount += 1;
            String clause = " ((year=" + fromYear + " AND month>=" + fromMonth + ") OR year>=" + (parseInt(fromYear) + 1) + ")";
            return clause;
        }
        return "";
    }

    public static String checkToYearAndMonth(String toYear, String toMonth){
        if(toYear != "All" && toMonth != "All") {
            ConnectDatabase.queryCount += 1;
            String clause = " ((year=" + toYear + " AND month<=" + toMonth + ") OR year<=" + (parseInt(toYear) - 1) + ")";
            return clause;
        }
        return "";
    }

    public static String addAndClause(int queryCount){
        if(queryCount > 0){
            return " AND";
        }
        return "";
    }
}
