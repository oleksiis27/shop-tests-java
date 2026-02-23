package com.shop.tests.ui;

import com.shop.pages.LoginPage;
import com.shop.pages.RegisterPage;
import com.shop.pages.components.NavBar;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

@Epic("Shop UI")
@Feature("Login")
public class LoginUiTest extends BaseUiTest {

    private final LoginPage loginPage = new LoginPage();
    private final NavBar navBar = new NavBar();

    @BeforeEach
    void setUp() {
        loginPage.openPage();
    }

    @Test
    @Story("Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Login with valid credentials should redirect to home page and show user name in navbar")
    @DisplayName("Login with valid credentials: redirect to home, user name visible")
    void loginWithValidCredentials() {
        loginPage.login(config.userEmail(), config.userPassword());

        navBar.shouldBeLoggedIn();
    }

    @Test
    @Story("Invalid Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Login with wrong password should display an error message")
    @DisplayName("Login with wrong password: error message displayed")
    void loginWithWrongPassword() {
        loginPage.login(config.userEmail(), "wrongpassword");

        loginPage.getErrorMessage();
    }

    @Test
    @Story("Navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clicking register link on login page should navigate to register page")
    @DisplayName("Click register link: navigate to register page")
    void navigateToRegisterPage() {
        loginPage.clickRegisterLink();

        webdriver().shouldHave(urlContaining("/register"));
        new RegisterPage().shouldBeDisplayed();
    }

    @Test
    @Story("Logout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("After logout, Login and Register links should be visible in navbar")
    @DisplayName("Logout: Login/Register links visible")
    void logoutShowsLoginRegisterLinks() {
        loginPage.login(config.userEmail(), config.userPassword());
        navBar.shouldBeLoggedIn();

        navBar.clickLogout();

        navBar.loginLinkShouldBeVisible();
        navBar.registerLinkShouldBeVisible();
    }
}