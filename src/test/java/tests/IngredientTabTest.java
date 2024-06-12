package tests;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import static org.junit.Assert.assertEquals;
import pages.*;

import java.util.Arrays;
import java.util.Collection;

import static utils.URL.*;

@RunWith(Parameterized.class)
public class IngredientTabTest {
    private DriverFactory driverFactory = new DriverFactory();
    private MainPage mainPage;
    private String clickSource;
    @Before
    public void setUp() {
        driverFactory.initDriver();
        RestAssured.baseURI = BASE_URI;
        WebDriver driver = driverFactory.getDriver();
        mainPage = new MainPage(driver);
    }

    public IngredientTabTest(String clickSource) {
        this.clickSource = clickSource;
    }
    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {"bun"},
                {"sauce"},
                {"main"},
        });
    }
    @Test
    public void testChangeIngredientTab(){
        WebDriver driver = driverFactory.getDriver();
        driver.get(BASE_URI);
        switch (clickSource){
            case "bun":
                mainPage.clickBunSection();
                break;
            case "sauce":
                mainPage.clickSauceSection();
                break;
            case "main":
                mainPage.clickMainSection();
                break;
        }
        boolean isTabActive = mainPage.isTabSelected(clickSource);
        assertEquals("Tab " + clickSource + " is not selected", true, mainPage.isTabSelected(clickSource));

    }
    @After
    public void tearDown() {
        if(driverFactory.getDriver() != null) {
            driverFactory.getDriver().quit();
        }
    }
}
