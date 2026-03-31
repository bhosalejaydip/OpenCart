package testBase;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;

public class BaseClass {

	public Properties p;
	public WebDriver driver;
	public Logger logger;

	// Master means all test runs--which contains all groups
	@BeforeClass(groups = { "Sanity", "Regression", "Master", "DataDriven" })
	@Parameters({ "os", "browser" })
	public void setup(String os, String br) throws IOException {
		// Loading configue.properties file
		FileReader file = new FileReader(".//src/test/resources//configue.properties");
		p = new Properties();
		p.load(file);

		logger = LogManager.getLogger(this.getClass());

		if (p.getProperty("execution_env").equalsIgnoreCase("remote")) {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			// os
			if (os.equalsIgnoreCase("windows")) {
				capabilities.setPlatform(Platform.WIN10);
			} else if (os.equalsIgnoreCase("mac")) {
				capabilities.setPlatform(Platform.MAC);

			} else {
				System.out.println("no matchine browser");
				return;
			}

			// browser
			switch (br.toLowerCase()) {
			case "chrome":
				capabilities.setBrowserName("chrome");
				break;
			case "edge":
				capabilities.setBrowserName("MicrosoftEdge");
				break;
			default:
				System.out.println("no matching browser");
				return;

			}
			
			driver = new RemoteWebDriver(new URL("http://192.168.1.3:4444/wd/hub"),capabilities);
		}
		if (p.getProperty("execution_env").equalsIgnoreCase("local")) {
			switch (br.toLowerCase()) {
			case "chrome":
				driver = new ChromeDriver();
				break;
			case "edge":
				driver = new EdgeDriver();
				break;
			case "firefox":
				driver = new FirefoxDriver();
				break;
			default:
				System.out.println("Invalid browser name..");
				return;
			}
		}

		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(p.getProperty("appURL"));
		driver.manage().window().maximize();
	}

	@AfterClass(groups = { "Sanity", "Regression", "Master" })
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	// Random alphabetic string
	public String randomeString() {
		return RandomStringUtils.randomAlphabetic(5);
	}

	// Random numeric string
	public String randomeNumber() {
		return RandomStringUtils.randomNumeric(10);
	}

	// Random alphanumeric password
	public String randomealphanNumberic() {
		String str = RandomStringUtils.randomAlphabetic(3);
		String num = RandomStringUtils.randomNumeric(3);
		return str + "@" + num;
	}

	// ---------------- Helper for ExtentReports / Screenshot ----------------

	/**
	 * Capture screenshot and return path for ExtentReport attach
	 * 
	 * @param testName
	 * @return screenshot path
	 */
	public String captureScreenshot(String testName) {
		String screenshotPath = System.getProperty("user.dir") + "/test-output/screenshots/" + testName + "_"
				+ System.currentTimeMillis() + ".png";
		try {
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File dest = new File(screenshotPath);

			// Create folder if not exists
			dest.getParentFile().mkdirs();

			FileUtils.copyFile(src, dest);
		} catch (Exception e) {
			logger.error("Failed to capture screenshot: " + e.getMessage());
		}
		return screenshotPath;
	}
}