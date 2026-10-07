package com.automation.salesforce.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ValidLoginTest extends BaseTest {
    @Test
    public void validCredentialsNavigateAwayFromLoginHost() throws Exception {
        String username = requireEnvironmentVariable("SF_USERNAME");
        String password = requireEnvironmentVariable("SF_PASSWORD");

        loginPage.open(loginUrl);
        loginPage.login(username, password);

        Assert.assertTrue(
                loginPage.waitForAuthenticationRedirect(loginUrl),
                "Expected valid credentials to navigate away from the Salesforce login host.");
    }
}