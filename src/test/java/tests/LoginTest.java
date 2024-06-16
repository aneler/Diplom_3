package tests;

import api.ApiEndpoints;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import json.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pages.*;
import utils.Utils;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static utils.URL.*;

@RunWith(Parameterized.class)
public class LoginTest {
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

    private User newUser;
    private String authToken;

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

    public LoginTest(String url, String clickSource) {
        this.url = url;
        this.clickSource = clickSource;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {BASE_URI, "header"},
                {BASE_URI, "mainPage"},
                {REGISTER_PAGE, "signUpPage"},
                {RESTORE_PASSWORD_PAGE, "restorePasswordPage"}
        });
    }

    @Test
    @DisplayName("Check login from different pages")
    public void testLogin(){
        WebDriver driver = driverFactory.getDriver();
        driver.get(url);
        newUser = new User(userEmail, password, userName);
        Response signUpResponse = apiEndpoints.sendRegisterRequest(newUser);
        signUpResponse.then().statusCode(200);

        //выбор страницы на которой дложен быть сделан клик в логин
        switch (clickSource) {
            case "header":
                header.clickAccountBth();
                break;
            case "mainPage":
                mainPage.clickLoginBtn();
                break;
            case "signUpPage":
                signUpPage.clickLoginLink();
                break;
            case "restorePasswordPage":
                restorePasswordPage.clickLoginLink();
                break;
        }
        //авторизоваться с данными только что зарегистрированного пользователя
        loginPage.fillAndSubmitLoginData(newUser.getEmail(), newUser.getPassword());
        authToken = Utils.fetchAuthTokenFromLocalStorage(driver, 10);

        if (authToken == null || authToken.isEmpty()) {
            System.err.println("Failed to fetch authToken from localStorage");
            return;
        }

        //открыть профиль пользователя и сравнить email с указанным при авторизации
        header.clickAccountBth();
        String currentEmail = profilePage.getEmail();
        assertEquals("Email пользователя в профиле не соответствует имени переданному при регистрации: ", userEmail, currentEmail);
    }

    @After
    public void tearDown() {
        if (authToken != null && !authToken.isEmpty()) {
            Response deleteUserResponse = apiEndpoints.sendDeleteUserRequest(authToken);
            deleteUserResponse.then().assertThat().statusCode(202);
        }
        if(driverFactory.getDriver() != null) {
            driverFactory.getDriver().quit();
        }
    }
}
