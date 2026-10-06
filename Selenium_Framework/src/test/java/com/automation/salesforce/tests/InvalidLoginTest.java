package com.automation.salesforce.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidLoginTest extends BaseTest {
    @Test
    public void invalidPasswordDisplaysLoginError() throws Exception {
        String username = requireEnvironmentVariable("SF_USERNAME");
        String invalidPassword = requireEnvironmentVariable("SF_INVALID_PASSWORD");

        loginPage.open(loginUrl);
        loginPage.login(username, invalidPassword);

        Assert.assertFalse(
                loginPage.waitForLoginError().isBlank(),
                "Expected Salesforce to display a nonempty login error.");
    }
}