package statsVisualiser.gui;

import statsVisualiser.DataQuery;

import java.sql.SQLException;
import java.util.HashMap;

public class TTestUI {

    public static void initializeTTestPanel() {
        // pull how many TTest are added
        int timeSeriesNum = TimeSeriesUI.getNumOfTimeSeries();

        // .get(whatever index the user choose in the combobox)
        HashMap<String, String> param = TimeSeriesUI.getTimeSeriesParams().get(1);
        // param.get("province")
        // param.get("city")
        // param.get("fromYear")
        // param.get("fromMonth")
        // param.get("toYear")
        // param.get("toMonth")

       //  String[][] data = DataQuery.getDataFromDatabase(param.get("province"), param.get("city"),
        //         param.get("fromYear"), param.get("fromMonth"),
           //     param.get("toYear"), param.get("toMonth"));

        // use data[][4] to get double value loop through data to get data[][4] to get all double values only
        // for (row : data)
        //      DoubleList.add(row[4])

        // remove all from panel in MainUI
        // Add updated selection and button to main
        // when button is clicked, output someting like
        // "Time Series Selected to compare: 1 & 5, output: "reject null hypothesis"
        // Call function in TTest file make it static

    }
}
