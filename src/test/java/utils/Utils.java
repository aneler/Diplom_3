package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Utils {
    public static boolean isElementVisible(WebDriver driver, By locator, Duration waitTime) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, waitTime);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean waitForUrlToBe(WebDriver driver, String url, int timeoutInSeconds) {
        Duration timeoutDuration = Duration.ofSeconds(timeoutInSeconds);
        WebDriverWait wait = new WebDriverWait(driver, timeoutDuration);
        try {
            wait.until(ExpectedConditions.urlToBe(url));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isTextPresent(WebDriver driver, String text, int timeoutInSeconds) {
        Duration timeoutDuration = Duration.ofSeconds(timeoutInSeconds);
        WebDriverWait wait = new WebDriverWait(driver, timeoutDuration);
        By locator = By.xpath("//*[contains(text(),'" + text + "')]");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element.getText().contains(text)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static String fetchAuthTokenFromLocalStorage(WebDriver driver, int timeout) {
        //приходится подождать немного, так как в яндекс браузере не отдается токен сразу, как в хром
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //waitForPageLoadComplete(driver, timeout);
        LocalStorage localStorage = ((WebStorage) driver).getLocalStorage();
        try {
            String accessToken = localStorage.getItem("accessToken");
            return accessToken;
        } catch (Exception e) {
            System.err.println("Error fetching authToken from localStorage: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
