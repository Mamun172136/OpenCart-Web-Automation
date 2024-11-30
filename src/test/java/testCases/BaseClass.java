package testCases;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager; // log4j
import org.apache.logging.log4j.Logger;   // log4j
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

public class BaseClass {
    public static WebDriver driver;
    public Logger logger;
    public Properties p;

    @BeforeClass(groups= {"Sanity", "Regrassion", "Master", "DataDriven"})
    @Parameters({"browser", "os"}) // Declare the parameter in the testng.xml file
    public void setup(String browser, String os) throws IOException {
        // Load the properties file
        FileReader file = new FileReader(System.getProperty("user.dir") + "//src//test//resources//config.properties");
        p = new Properties();
        p.load(file);

        // Initialize logger
        logger = LogManager.getLogger(this.getClass());

        // Browser initialization based on parameter
        if (browser == null) {
            // Default to ChromeDriver if no browser parameter is passed
            browser = "chrome";
        }
        
        if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
			DesiredCapabilities capabilities=new DesiredCapabilities();
			
			//os
			if(os.equalsIgnoreCase("windows"))
			{
				capabilities.setPlatform(Platform.WIN11);
			}
			else if (os.equalsIgnoreCase("mac"))
			{
				capabilities.setPlatform(Platform.MAC);
			}
			else if (os.equalsIgnoreCase("Linux"))
			{
				capabilities.setPlatform(Platform.LINUX);
			}
			else
			{
				System.out.println("No matching os");
				return;
			}
			
			//browser
			switch(browser.toLowerCase())
			{
			case "chrome": capabilities.setBrowserName("chrome"); break;
			case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
			case "firefox": capabilities.setBrowserName("FireFox"); break;
			default: System.out.println("No matching browser"); return;
			}
			
			driver=new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
		}
        
        
        if(p.getProperty("execution_env").equalsIgnoreCase("local")) {
        	
        	   switch (browser.toLowerCase()) {
               case "chrome":
                   driver = new ChromeDriver();
                   logger.info("Chrome browser launched");
                   break;
               case "edge":
                   driver = new EdgeDriver();
                   logger.info("Edge browser launched");
                   break;
               case "firefox":
                   driver = new FirefoxDriver();
                   logger.info("Firefox browser launched");
                   break;
               default:
                   logger.error("Invalid browser name provided, defaulting to Chrome");
                   driver = new ChromeDriver();
           }

           // WebDriver setup
           driver.manage().deleteAllCookies();
           driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
           driver.get("https://tutorialsninja.com/demo/");
           driver.manage().window().maximize();
        }

     
    }

    @AfterClass(groups= {"Sanity", "Regrassion", "Master", "DataDriven"})
    public void tearDown() {
        // Close the browser after tests are completed
        if (driver != null) {
            driver.quit();
            logger.info("Browser closed successfully");
        }
    }

    // Random string generator
    public String randomString() {
        return RandomStringUtils.randomAlphanumeric(5);
    }

    // Random numeric generator
    public String randomNumber() {
        return RandomStringUtils.randomNumeric(9);
    }

    // Random alphanumeric generator
    public String randomAlphaNumeric() {
        String generatedString = RandomStringUtils.randomAlphabetic(3);
        String generatedNumber = RandomStringUtils.randomNumeric(3);
        return generatedString + "@" + generatedNumber;
    }
    
	public String captureScreen(String tname) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
		File targetFile=new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
			
		return targetFilePath;

	}
}
