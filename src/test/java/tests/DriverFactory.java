package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {
    private WebDriver driver;

    public void initDriver() {
        if ("firefox".equals(System.getProperty("browser"))) {
            initFirefox();
        } else if ("yandex".equals(System.getProperty("browser"))){
            initYandex();
        } else {
            initChrome();
        }
    }

    private void initFirefox() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions().configureFromEnv();
        driver = new FirefoxDriver(options);
    }

    private void initChrome(){
        WebDriverManager.chromedriver().setup();;
        driver = new ChromeDriver();
    }

    private void initYandex(){
        //WebDriverManager.chromedriver().driverVersion("122.0.6261.128").setup();
        WebDriverManager.chromedriver().driverVersion(System.getProperty("driver.version")).setup();
        var options = new ChromeOptions();
        //options.setBinary("/Applications/Yandex.app/Contents/MacOS/Yandex");
        options.setBinary(System.getProperty("webdriver-yandex.bin"));
        driver = new ChromeDriver(options);
    }
    public WebDriver getDriver() {
        return driver;
    }
}
