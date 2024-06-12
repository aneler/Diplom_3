package tests;

import api.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pages.Header;
import pages.LoginPage;
import pages.SignUpPage;
import utils.Utils;
import com.github.javafaker.Faker;
import static org.junit.Assert.assertEquals;
import static utils.URL.BASE_URI;

public class SignUpTest {
    //test data
    static Faker faker = new Faker();
    static String userName = faker.name().firstName();
    static String userEmail = faker.internet().emailAddress();
    static String password = "123qwe!Q";
    private DriverFactory driverFactory = new DriverFactory();
    ApiEndpoints apiEndpoints = new ApiEndpoints();
    private Header header;
    private LoginPage loginPage;
    private SignUpPage signUpPage;
    @Before
    public void setUp() {
        driverFactory.initDriver();
        RestAssured.baseURI = BASE_URI;
        WebDriver driver = driverFactory.getDriver();
        header = new Header(driver);
        loginPage = new LoginPage(driver);
        signUpPage = new SignUpPage(driver);
    }

    @Test
    public void testSignUpWithCorrectData(){
        WebDriver driver = driverFactory.getDriver();
        driver.get("https://stellarburgers.nomoreparties.site/");
        header.clickAccountBth();
        boolean isPageLoaded = Utils.waitForUrlToBe(driver, "https://stellarburgers.nomoreparties.site/login", 10);
        loginPage.clickSignUpLink();
        //проверить, что станица регистрации загружена
        Utils.waitForUrlToBe(driver, "https://stellarburgers.nomoreparties.site/login", 10);
        //ввести тестовые данные в форму регистрации
        signUpPage.fillAndSubmitUserData(userName, userEmail, password);
        Utils.waitForUrlToBe(driver, "https://stellarburgers.nomoreparties.site/login", 10);
        boolean isSignUpSuccessful = Utils.isTextPresent(driver, "Вход", 10);
        assertEquals("Должна появится форма авторизации: ", true, isSignUpSuccessful);

        header.clickAccountBth();
        //авторизоваться с данными только что зарегистрированного пользователя
        loginPage.fillAndSubmitLoginData(userEmail, password);
        String authToken = Utils.fetchAuthTokenFromLocalStorage(driver, 20);

        if (authToken == null || authToken.isEmpty()) {
            System.err.println("Failed to fetch authToken from localStorage");
            return;
        }

        //проверить, что на странице есть нужный текст
        boolean isTextVisible = Utils.isTextPresent(driver, "Соберите бургер", 10);
        assertEquals("Текст 'Соберите бургер' не появился: ", true, isTextVisible);
        //удалить созданного пользователя
        Response deleteUserResponse = apiEndpoints.sendDeleteUserRequest(authToken);
        deleteUserResponse.then().assertThat().statusCode(202);
    }
    @Test
    public void testSignUpIncorrectPassword(){
        WebDriver driver = driverFactory.getDriver();
        driver.get("https://stellarburgers.nomoreparties.site/");
        header.clickAccountBth();
        boolean isPageLoaded = Utils.waitForUrlToBe(driver, "https://stellarburgers.nomoreparties.site/login", 10);
        loginPage.clickSignUpLink();
        //проверить, что станица регистрации загружена
        Utils.waitForUrlToBe(driver, "https://stellarburgers.nomoreparties.site/login", 10);
        //ввести тестовые данные в форму регистрации, пароль короче 6 символов
        signUpPage.fillAndSubmitUserData(userName, userEmail, "passw");
        boolean isErrorMessageVisible = signUpPage.isPasswordErrorMessageVisible();
        assertEquals("Сообщение об ошибке не отображается: ", true, isErrorMessageVisible);
    }

    @After
    public void tearDown() {
        if(driverFactory.getDriver() != null) {
            driverFactory.getDriver().quit();
        }
    }
}
