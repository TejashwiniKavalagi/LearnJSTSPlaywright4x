package com.automation.salesforce.pages;

import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//input[@id='username']")
    private WebElement usernameField;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@id='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[@id='error']")
    private WebElement loginError;

    public LoginPage(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        PageFactory.initElements(driver, this);
    }

    public void open(String loginUrl) {
        try {
            driver.get(loginUrl);
            wait.until(ExpectedConditions.visibilityOf(usernameField));
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to open the Salesforce login page.", exception);
        }
    }

    public void login(String username, String password) {
        try {
            wait.until(ExpectedConditions.visibilityOf(usernameField));
            usernameField.clear();
            usernameField.sendKeys(username);
            passwordField.clear();
            passwordField.sendKeys(password);
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to submit the Salesforce login form.", exception);
        }
    }

    public boolean waitForAuthenticationRedirect(String loginUrl) {
        String loginHost = URI.create(loginUrl).getHost();
        if (loginHost == null) {
            throw new IllegalArgumentException("The configured login URL must include a host.");
        }

        try {
            return wait.until(currentDriver -> {
                String currentHost = URI.create(currentDriver.getCurrentUrl()).getHost();
                return currentHost != null && !loginHost.equalsIgnoreCase(currentHost);
            });
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Login did not navigate away from the login host.", exception);
        }
    }

    public String waitForLoginError() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(loginError)).getText().trim();
        } catch (WebDriverException exception) {
            throw new IllegalStateException("A visible Salesforce login error was not found.", exception);
        }
    }
}