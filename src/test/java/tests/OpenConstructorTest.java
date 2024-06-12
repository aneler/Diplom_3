package tests;

import api.ApiEndpoints;
import com.github.javafaker.Faker;
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
public class OpenConstructorTest {
    //test data
    static Faker faker = new Faker();
    static String userName = faker.name().firstName();
    static String userEmail = faker.internet().emailAddress();
    static String password = "123qwe!Q";
    private DriverFactory driverFactory = new DriverFactory();
    ApiEndpoints apiEndpoints = new ApiEndpoints();
    private Header header;
    private LoginPage loginPage;
    private MainPage mainPage;
    private String clickSource;

    @Before
    public void setUp() {
        driverFactory.initDriver();
        RestAssured.baseURI = BASE_URI;
        WebDriver driver = driverFactory.getDriver();
        header = new Header(driver);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
    }

    public OpenConstructorTest(String clickSource) {
        this.clickSource = clickSource;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {"burgerLogo"},
                {"constructor"}
        });
    }

    @Test
    public void testOpenBurgerConstructor(){
        WebDriver driver = driverFactory.getDriver();
        driver.get(BASE_URI);
        User newUser = new User(userEmail, password, userName);
        Response signUpResponse = apiEndpoints.sendRegisterRequest(newUser);
        signUpResponse.then().statusCode(200);

        //авторизоваться и открыть личный кабинет пользователя
        mainPage.clickLoginBtn();
        loginPage.fillAndSubmitLoginData(newUser.getEmail(), newUser.getPassword());
        String authToken = Utils.fetchAuthTokenFromLocalStorage(driver, 10);
        if (authToken == null || authToken.isEmpty()) {
            System.err.println("Failed to fetch authToken from localStorage");
            return;
        }

        header.clickAccountBth();
        //в яндекс браузере долго загружается url личного кабинета
        Utils.waitForUrlToBe(driver, PROFILE_PAGE, 30);
        String currentUrl = driver.getCurrentUrl();
        assertEquals(PROFILE_PAGE, currentUrl);

        //выбор элемента на которой должен быть сделан клик
        switch (clickSource) {
            case "burgerLogo":
                header.clickBurgerLogo();
                break;
            case "constructor":
                header.clickConstructor();
                break;
        }
        //проверить, что url соответствует ожидаемому
        String curUrl = driver.getCurrentUrl();
        assertEquals(BASE_URI, curUrl);
        assertEquals(true, Utils.isTextPresent(driver, "Соберите бургер", 3));

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
