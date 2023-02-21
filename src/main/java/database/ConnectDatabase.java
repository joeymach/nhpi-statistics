package database;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;
import static java.lang.Integer.parseInt;

public class ConnectDatabase {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public void readDataBase() throws Exception {
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
                String[] data=lineText.split(",");

                int year = parseInt(data[0].split("-")[0]);
                int month = parseInt(data[0].split("-")[1]);

                String[] location = data[1].replaceAll("[\"]", "").split(", ");

                if(location.length > 1) {
                    length = location.length - 1;
                } else {
                    length = 0;
                }

                String city = String.join(", ", Arrays.copyOfRange(location, 0, location.length - 1));
                String province = location[location.length - 1];

                System.out.println("city " + city);
                System.out.println("prov  " + province);

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

}
