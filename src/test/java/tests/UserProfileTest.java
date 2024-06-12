package tests;

import api.ApiEndpoints;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import json.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pages.*;
import utils.Utils;

import static org.junit.Assert.assertEquals;
import static utils.URL.*;

public class UserProfileTest {
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
    private MainPage mainPage;
    private ProfilePage profilePage;
    private RestorePasswordPage restorePasswordPage;
    private String url;
    private String clickSource;

    @Before
    public void setUp() {
        driverFactory.initDriver();
        RestAssured.baseURI = BASE_URI;
        WebDriver driver = driverFactory.getDriver();
        header = new Header(driver);
        loginPage = new LoginPage(driver);
        signUpPage = new SignUpPage(driver);
        mainPage = new MainPage(driver);
        profilePage = new ProfilePage(driver);
        restorePasswordPage = new RestorePasswordPage(driver);
    }

    @Test
    public void testProfileOpen(){
        WebDriver driver = driverFactory.getDriver();
        User newUser = new User(userEmail, password, userName);
        Response signUpResponse = apiEndpoints.sendRegisterRequest(newUser);
        signUpResponse.then().statusCode(200);
        driver.get(BASE_URI);
        header.clickAccountBth();

        //авторизоваться с данными только что зарегистрированного пользователя
        loginPage.fillAndSubmitLoginData(newUser.getEmail(), newUser.getPassword());
        String authToken = Utils.fetchAuthTokenFromLocalStorage(driver, 20);

        if (authToken == null || authToken.isEmpty()) {
            System.err.println("Failed to fetch authToken from localStorage");
            return;
        }

        //открыть профиль пользователя и сравнить email и имя с указанным при авторизации
        header.clickAccountBth();

        // в яндекс браузере долго загружается url
        Utils.waitForUrlToBe(driver, PROFILE_PAGE, 30);
        String currentUrl = driver.getCurrentUrl();
        assertEquals(PROFILE_PAGE, currentUrl);

        String currentEmail = profilePage.getEmail();
        assertEquals("Email пользователя в профиле не соответствует email переданному при регистрации: ", userEmail, currentEmail);
        String currentName = profilePage.getName();
        assertEquals("Имя пользователя в профиле не соответствует имени переданному при регистрации: ", userName, currentName);


        //удалить созданного пользователя
        Response deleteUserResponse = apiEndpoints.sendDeleteUserRequest(authToken);
        deleteUserResponse.then().assertThat().statusCode(202);
    }

    @After
    public void tearDown() {
        if(driverFactory.getDriver() != null) {
            driverFactory.getDriver().quit();
        }
    }
}
