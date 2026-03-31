package utilities;

import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name="LoginData")
    public String[][] getData() throws Exception
    {
        String path = System.getProperty("user.dir") + "/testData/opencartdemo.xlsx";

        ExcelUtility xl = new ExcelUtility(path);

        int totalrows = xl.getRowCount("Sheet1");
        int totalcols = xl.getCellCount("Sheet1", 1);

        String logindata[][] = new String[totalrows][totalcols];

        for(int i=1;i<=totalrows;i++)
        {
            for(int j=0;j<totalcols;j++)
            {
                logindata[i-1][j] = xl.getCellData("Sheet1", i, j).trim();
            }
        }

        return logindata;
    }
}