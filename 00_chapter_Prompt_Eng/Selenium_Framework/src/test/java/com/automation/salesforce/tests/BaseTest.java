package com.automation.salesforce.tests;

import java.time.Duration;

import com.automation.salesforce.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public abstract class BaseTest {
    private static final String DEFAULT_LOGIN_URL = "https://login.salesforce.com/?locale=in";
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    protected WebDriver driver;
    protected LoginPage loginPage;
    protected String loginUrl;

    @BeforeTest(alwaysRun = true)
    public void configureSuite() {
        String configuredUrl = System.getenv("SF_LOGIN_URL");
        loginUrl = configuredUrl == null || configuredUrl.isBlank()
                ? DEFAULT_LOGIN_URL
                : configuredUrl.trim();
    }

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        try {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            loginPage = new LoginPage(driver, WAIT_TIMEOUT);
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to start Chrome WebDriver.", exception);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    protected String requireEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Required environment variable is not set: " + name);
        }
        return value;
    }
}