package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Utils;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private By userEmail = By.xpath("//div/label[contains(text(), 'Email')]/following-sibling::input");
    private By userPassword = By.xpath("//input[@type='password']");
    private By loginBtn = By.xpath("//button[text()='Войти']");
    private By signUpLink = By.xpath("//a[contains(@href, '/register')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickSignUpLink(){
        Utils.isElementVisible(driver, signUpLink, Duration.ofSeconds(10));
        driver.findElement(signUpLink).click();
    }
    public void fillUserEmail(String email){
        Utils.isElementVisible(driver, userEmail, Duration.ofSeconds(3));
        driver.findElement(userEmail).sendKeys(email);
    }
    public void fillUserPassword(String password){
        Utils.isElementVisible(driver, userPassword, Duration.ofSeconds(3));
        driver.findElement(userPassword).sendKeys(password);
    }
    public void clickLoginBtn(){
        Utils.isElementVisible(driver, loginBtn, Duration.ofSeconds(3));
        driver.findElement(loginBtn).click();
    }
    public void fillAndSubmitLoginData(String email, String password){
        fillUserEmail(email);
        fillUserPassword(password);
        clickLoginBtn();
    }
}
