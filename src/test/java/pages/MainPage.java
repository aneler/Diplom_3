package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Utils;

import java.time.Duration;

public class MainPage {
    private WebDriver driver;
    private By login = By.xpath("//button[contains(text(), 'Войти в аккаунт')]");
    private By bun = By.xpath("//div/span[contains(text(), 'Булки')]");
    private By sauce = By.xpath("//div/span[contains(text(), 'Соусы')]");
    private By main = By.xpath("//div/span[contains(text(), 'Начинки')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickLoginBtn(){
        Utils.isElementVisible(driver, login, Duration.ofSeconds(3));
        driver.findElement(login).click();
    }

    public void clickBunSection(){
        Utils.isElementVisible(driver, bun, Duration.ofSeconds(3));
        if (!isTabSelected("bun")) {
            driver.findElement(bun).click();
        }
    }
    public void clickSauceSection(){
        Utils.isElementVisible(driver, sauce, Duration.ofSeconds(3));
        if (!isTabSelected("sauce")) {
            driver.findElement(sauce).click();
        }
    }
    public void clickMainSection(){
        Utils.isElementVisible(driver, main, Duration.ofSeconds(3));
        if (!isTabSelected("main")) {
            driver.findElement(main).click();
        }
    }
    public boolean isTabSelected(String tab) {
        By locator;
        switch (tab.toLowerCase()) {
            case "bun":
                locator = bun;
                break;
            case "sauce":
                locator = sauce;
                break;
            case "main":
                locator = main;
                break;
            default:
                throw new IllegalArgumentException("Unknown tab name: " + tab);
        }
        WebElement element = driver.findElement(locator);
        WebElement parentElement = element.findElement(By.xpath(".."));
        return parentElement.getAttribute("class").contains("tab_tab_type_current");
    }
}
