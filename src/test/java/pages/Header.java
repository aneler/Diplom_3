package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Utils;

import java.time.Duration;

public class Header {
    private WebDriver driver;
    public By account = By.xpath("//a[contains(@class, 'AppHeader_header_') and contains(@href, 'account')]/p");
    public By burgerLogo = By.xpath("//div[contains(@class, 'AppHeader_header__logo')]");
    public By constructor = By.xpath("//p[contains(text(), 'Конструктор')]");

    public Header(WebDriver driver) {
        this.driver = driver;
    }

    public void clickAccountBth(){
        boolean isVisible = Utils.isElementVisible(driver, account, Duration.ofSeconds(10));
        if (isVisible) {
            driver.findElement(account).click();
        } else {
            System.out.println("Элемент не появился на странице в течении 10 секунд");
        }
    }

    public void clickBurgerLogo(){
        Utils.isElementVisible(driver, burgerLogo, Duration.ofSeconds(3));
        driver.findElement(burgerLogo).click();
    }
    public void clickConstructor(){
        Utils.isElementVisible(driver, constructor, Duration.ofSeconds(3));
        driver.findElement(constructor).click();
    }
}
