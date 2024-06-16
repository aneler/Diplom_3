package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Utils;

import java.time.Duration;

public class SignUpPage {
    private WebDriver driver;
    private By userName = By.xpath("//div/label[contains(text(), 'Имя')]/following-sibling::input");
    private By userEmail = By.xpath("//div/label[contains(text(), 'Email')]/following-sibling::input");
    private By userPassword = By.xpath("//input[@type='password']");
    private By signUpBtn = By.xpath("//button[text()='Зарегистрироваться']");
    private By incorrectPasswordMsg = By.xpath("//div[contains(@class, 'input_type_password')]/following-sibling::p[contains(text(), 'Некорректный пароль')]");
    private By loginLink = By.xpath("//a[@href='/login']");

    public SignUpPage(WebDriver driver) {
        this.driver = driver;
    }
    public void fillUserName(String name){
        //wait for name input field
        Utils.isElementVisible(driver, userName, Duration.ofSeconds(3));
        //fill name field
        driver.findElement(userName).sendKeys(name);
    }
    public void fillUserEmail(String email){
        //wait for email input field
        Utils.isElementVisible(driver, userEmail, Duration.ofSeconds(3));
        //fill email field
        driver.findElement(userEmail).sendKeys(email);
    }
    public void fillUserPassword(String password){
        //wait for password input field
        Utils.isElementVisible(driver, userPassword, Duration.ofSeconds(3));
        //fill password field
        driver.findElement(userPassword).sendKeys(password);
    }
    public void clickSignUpBtn(){
        Utils.isElementVisible(driver, signUpBtn, Duration.ofSeconds(3));
        driver.findElement(signUpBtn).click();
    };

    public void fillAndSubmitUserData(String name, String email, String password){
        fillUserName(name);
        fillUserEmail(email);
        fillUserPassword(password);
        clickSignUpBtn();
    }

    public boolean isPasswordErrorMessageVisible(){
        boolean isMessageVisible = Utils.isElementVisible(driver, incorrectPasswordMsg, Duration.ofSeconds(3));
        return isMessageVisible;
    }

    public void clickLoginLink(){
        Utils.isElementVisible(driver, loginLink, Duration.ofSeconds(10));
        driver.findElement(loginLink).click();
    }
}
