package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Utils;

import java.time.Duration;

public class RestorePasswordPage {
    private WebDriver driver;
    private By loginLink = By.xpath("//a[contains(@href, '/login')]");

    public RestorePasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickLoginLink(){
        Utils.isElementVisible(driver, loginLink, Duration.ofSeconds(10));
        driver.findElement(loginLink).click();
    }
}
