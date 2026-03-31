package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import testBase.BaseClass;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentReportManager implements ITestListener {

    private static ExtentReports extent;
    private static Map<String, ExtentTest> testMap = new HashMap<>();


    // Report start
    @Override
    public void onStart(ITestContext context) {

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        String reportPath = System.getProperty("user.dir")
                + "/test-output/ExtentReports/AutomationReport_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

        spark.config().setDocumentTitle("OpenCart Automation Report");
        spark.config().setReportName("OpenCart Automation Results");
        spark.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Project", "OpenCart Automation");
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));

        String browser = context.getCurrentXmlTest().getParameter("browser");
        String os = context.getCurrentXmlTest().getParameter("os");

        extent.setSystemInfo("Browser", browser);
        extent.setSystemInfo("OS", os);
    }


    // Test start
    @Override
    public void onTestStart(ITestResult result) {

        // Show ClassName + MethodName
        String testName = result.getTestClass().getRealClass().getSimpleName()
                + "." + result.getMethod().getMethodName();

        ExtentTest test = extent.createTest(testName);

        // Assign groups
        String[] groups = result.getMethod().getGroups();

        for(String group : groups) {
            test.assignCategory(group);
        }

        testMap.put(result.getMethod().getMethodName(), test);

        test.info("Test Started: " + testName);
    }


    // Test success
    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTest test = testMap.get(result.getMethod().getMethodName());

        if (test != null)
            test.pass("Test Passed");
    }


    // Test failure
    @Override
    public void onTestFailure(ITestResult result) {

        ExtentTest test = testMap.get(result.getMethod().getMethodName());

        if (test != null) {

            test.fail("Test Failed: " + result.getThrowable());

            Object testClass = result.getInstance();

            WebDriver driver = ((BaseClass) testClass).driver;

            try {

                String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

                String screenshotPath = System.getProperty("user.dir")
                        + "/test-output/screenshots/"
                        + result.getMethod().getMethodName()
                        + "_" + timestamp + ".png";

                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                File dest = new File(screenshotPath);

                FileUtils.copyFile(src, dest);

                test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");

            }
            catch (IOException e) {

                test.warning("Screenshot failed: " + e.getMessage());
            }
        }
    }


    // Test skipped
    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentTest test = testMap.get(result.getMethod().getMethodName());

        if (test != null)
            test.skip("Test Skipped: " + result.getThrowable());
    }


    // Finish
    @Override
    public void onFinish(ITestContext context) {

        extent.flush();
    }

}