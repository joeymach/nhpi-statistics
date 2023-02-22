package database;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;

import static java.lang.Double.parseDouble;
import static java.lang.Double.valueOf;
import static java.lang.Integer.parseInt;

public class ConnectDatabase {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

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

            PreparedStatement statement=connection.prepareStatement(sql);

            BufferedReader lineReader=new BufferedReader(new FileReader(filePath));

            String lineText=null;
            int count=0;

            int length = 0;

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

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Setup the connection with the DB
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost/nhpi?"
                            + "user=root&password=");

            connection.setAutoCommit(false);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        return this.connection;
    }

    public static String getQuery(String province, String city, String fromYear,
                           String fromMonth, String toYear, String toMonth) {
        String query = "SELECT * from nhpi";
        if (province != "All" || city != "All" ||
                fromYear != "All" || fromMonth != "All" ||
                toYear != "All" || toMonth != "All") {
            query += " WHERE";
        }
        int queryCount = 0;

        if(province != "All") {
            if (queryCount > 0) {
                query += " AND";
            }
            query += " province=\"" + province + "\"";
            queryCount += 1;
        }
        if(city != "All") {
            if (queryCount > 0) {
                query += " AND";
            }
            query += " city=\"" + city + "\"";
            queryCount += 1;
        }

        if(fromYear != "All" && fromMonth != "All") {
            if (queryCount > 0) {
                query += " AND";
            }
            query += " ((year=" + fromYear + " AND month>=" + fromMonth + ") OR year>=" + (parseInt(fromYear) + 1) + ")";
            queryCount += 1;
        }
        if(toYear != "All" && toMonth != "All") {
            if (queryCount > 0) {
                query += " AND";
            }
            query += " ((year=" + toYear + " AND month<=" + toMonth + ") OR year<=" + (parseInt(toYear) - 1) + ")";
            queryCount += 1;
        }

        query += ";";
        return query;
    }
}
