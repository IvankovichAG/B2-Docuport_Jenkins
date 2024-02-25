package app.docuport.utilities;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.URL;

import static java.lang.Integer.*;

public class Driver {
        static String browser;

        private Driver() {
        }

        private static WebDriver driver;
        private static ChromeOptions chromeOptions;
        private static FirefoxOptions firefoxOptions;

        private static DesiredCapabilities desiredCapabilities;

        public static WebDriver getDriver() {
            if (driver == null) {
                if (System.getProperty("BROWSER") == null) {
                    browser = ConfigurationReader.getProperty("browser");
                } else {
                    browser = System.getProperty("BROWSER");
                }
                System.out.println("Browser: " + browser);
                switch (browser) {

                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        driver = new ChromeDriver();
                        driver.manage().window().maximize();
                       // driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(valueOf(ConfigurationReader.getProperty("timeout"))));
                        break;

                    case "chrome-headless":
//                    WebDriverManager.chromedriver().setup();
//                    driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("--headless"); // Enable headless mode
                        //options.addArguments("start-maximized"); // maximize
                        WebDriverManager.chromedriver().setup();
                        driver = new ChromeDriver(options);
                        break;

                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driver = new FirefoxDriver();
                        break;

                    case "firefox-headless":
//                    WebDriverManager.firefoxdriver().setup();
//                    driver = new FirefoxDriver(new FirefoxOptions().setHeadless(true));
                        FirefoxOptions options2 = new FirefoxOptions();
                        options2.addArguments("--headless"); // Enable headless mode
                        //options.addArguments("start-maximized"); // maximize
                        WebDriverManager.firefoxdriver().setup();
                        driver = new FirefoxDriver(options2);
                        break;

                    case "ie":
                        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                            throw new WebDriverException("Your operating system does not support the requested browser");
                        }
                        WebDriverManager.iedriver().setup();
                        driver = new InternetExplorerDriver();
                        break;

                    case "edge":
                        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                            throw new WebDriverException("Your operating system does not support the requested browser");
                        }
                        WebDriverManager.edgedriver().setup();
                        driver = new EdgeDriver();
                        break;

                    // Need to enable the 'Allow Remote Automation' option in Safari's Develop menu to control Safari via WebDriver.
                    // Choose Safari > Preferences, and on the Advanced tab, select “Show Develop menu in menu bar.” For details, see Safari Help. Choose Develop > Allow Remote Automation.
                    case "safari":
                        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                            throw new WebDriverException("Your operating system does not support the requested browser");
                        }
                        WebDriverManager.getInstance(SafariDriver.class).setup();
                        driver = new SafariDriver();
                        break;


                    /**
                     * These added because of EC@2 Jenkins on Linux was not running the ones above because of graphical issues.
                     */
                    case "chrome-linux":
                        WebDriverManager.chromedriver().setup();
                        chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--headless");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        driver = new ChromeDriver(chromeOptions);
                        break;

                    case "firefox-linux":
                        WebDriverManager.firefoxdriver().setup();
                        firefoxOptions = new FirefoxOptions();
                        firefoxOptions.addArguments("--headless");
                        firefoxOptions.addArguments("--disable-gpu");
                        firefoxOptions.addArguments("--no-sandbox");
                        driver = new FirefoxDriver(firefoxOptions);
                        break;

                    case "remote-chrome":
                        try {
                            // assign your grid server address
                            String gridAddress = "3.81.122.15";
                            URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                            desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("chrome");
                            driver = new RemoteWebDriver(url, desiredCapabilities);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "remote-firefox":
                        try {
                            // assign your grid server address
                            String gridAddress = "3.81.122.15";
                            URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("firefox");
                            driver = new RemoteWebDriver(url, desiredCapabilities);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "remote-chrome-linux":
                        try {
                            // assign your grid server address
                            String gridAddress = "3.81.122.15";
                            URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                            chromeOptions = new ChromeOptions();
                            chromeOptions.addArguments("--headless");
                            chromeOptions.addArguments("--no-sandbox");
                            chromeOptions.addArguments("--disable-dev-shm-usage");
                            desiredCapabilities.merge(chromeOptions);
                            driver = new RemoteWebDriver(url, desiredCapabilities);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "remote-firefox-linux":
                        try {
                            // assign your grid server address
                            String gridAddress = "3.81.122.15";
                            URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                            desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("firefox");
                            FirefoxOptions firefoxOptions = new FirefoxOptions();
                            firefoxOptions.addArguments("--headless");
                            firefoxOptions.addArguments("--disable-gpu");
                            firefoxOptions.addArguments("--no-sandbox");
                            desiredCapabilities.merge(firefoxOptions);
                            driver = new RemoteWebDriver(url, desiredCapabilities);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            return driver;
        }

        public static void closeDriver() {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }