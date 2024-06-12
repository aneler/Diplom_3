package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Utils;

import java.time.Duration;

public class ProfilePage {
    private WebDriver driver;
    private By name = By.xpath("//div/label[contains(text(), 'Имя')]/following-sibling::input");
    private By email = By.xpath("//div/label[contains(text(), 'Логин')]/following-sibling::input");
    private By logout = By.xpath("//button[contains(text(), 'Выход')]");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getName(){
        Utils.isElementVisible(driver, name, Duration.ofSeconds(10));
        String currentName = driver.findElement(name).getAttribute("value");
        return currentName;
    }

    public String getEmail(){
        Utils.isElementVisible(driver, email, Duration.ofSeconds(10));
        String currentEmail = driver.findElement(email).getAttribute("value");
        return currentEmail;
    }
    public void clickLogoutBtn(){
        Utils.isElementVisible(driver, logout, Duration.ofSeconds(10));
        driver.findElement(logout).click();
    }
}
