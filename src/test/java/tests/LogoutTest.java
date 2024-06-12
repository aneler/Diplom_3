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

public class LogoutTest {
    //test data
    static Faker faker = new Faker();
    static String userName = faker.name().firstName();
    static String userEmail = faker.internet().emailAddress();
    static String password = "123qwe!Q";
    private DriverFactory driverFactory = new DriverFactory();
    ApiEndpoints apiEndpoints = new ApiEndpoints();
    private Header header;
    private LoginPage loginPage;
    private ProfilePage profilePage;

    @Before
    public void setUp() {
        driverFactory.initDriver();
        RestAssured.baseURI = BASE_URI;
        WebDriver driver = driverFactory.getDriver();
        header = new Header(driver);
        loginPage = new LoginPage(driver);
        profilePage = new ProfilePage(driver);
    }

    @Test
    public void testLogout(){
        WebDriver driver = driverFactory.getDriver();
        User newUser = new User(userEmail, password, userName);
        Response signUpResponse = apiEndpoints.sendRegisterRequest(newUser);
        signUpResponse.then().statusCode(200);
        driver.get(BASE_URI);
        header.clickAccountBth();

        //авторизоваться с данными только что зарегистрированного пользователя
        loginPage.fillAndSubmitLoginData(newUser.getEmail(), newUser.getPassword());
        String authToken = Utils.fetchAuthTokenFromLocalStorage(driver, 10);

        if (authToken == null || authToken.isEmpty()) {
            System.err.println("Failed to fetch authToken from localStorage");
            return;
        }

        //открыть профиль пользователя и сравнить email с указанным при авторизации
        header.clickAccountBth();
        String currentEmail = profilePage.getEmail();
        assertEquals("Email пользователя в профиле не соответствует имени переданному при регистрации: ", userEmail, currentEmail);

        profilePage.clickLogoutBtn();
        assertEquals(true, Utils.isTextPresent(driver,"Вход", 3));
        String currentUrl = driver.getCurrentUrl();
        assertEquals(currentUrl, LOGIN_PAGE);
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
