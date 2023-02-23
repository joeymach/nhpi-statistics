package TTest;

import database.ConnectDatabase;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.inference.TTest;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ConnectDatabase mysql = new ConnectDatabase();
        Connection connection = mysql.getConnection();

        String queryOne = "SELECT year, month, city, province, value FROM nhpi WHERE province='British Columbia'";
        String queryTwo = "SELECT year, month, city, province, value FROM nhpi WHERE province='Alberta'";

        PreparedStatement statementOne = connection.prepareStatement(queryOne);
        ResultSet resultOne = statementOne.executeQuery(queryOne);
        PreparedStatement statementTwo = connection.prepareStatement(queryTwo);
        ResultSet resultTwo = statementTwo.executeQuery(queryTwo);

        SummaryStatistics statsOne = new SummaryStatistics();
        SummaryStatistics statsTwo = new SummaryStatistics();

        while (resultOne.next()) {
            statsOne.addValue(resultOne.getDouble(5));
        }
        while (resultTwo.next()) {
            statsTwo.addValue(resultTwo .getDouble(5));
        }

        TTest tTest = new TTest();
        double pValue = tTest.tTest(statsOne, statsTwo);

        System.out.println("p-value: " + pValue);
        if (pValue < 0.05) {
            System.out.println("We can reject the null hypothesis");
        } else {
            System.out.println("We cannot reject the null hypothesis");
        }

        connection.close();
    }
}
