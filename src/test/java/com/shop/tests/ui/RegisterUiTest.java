package com.shop.tests.ui;

import com.shop.helpers.TestDataHelper;
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
@Feature("Registration")
public class RegisterUiTest extends BaseUiTest {

    private final RegisterPage registerPage = new RegisterPage();
    private final NavBar navBar = new NavBar();

    @BeforeEach
    void setUp() {
        registerPage.openPage();
    }

    @Test
    @Story("Valid Registration")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Register a new user should auto-login and redirect to home page")
    @DisplayName("Register new user → auto-login, redirect to home")
    void registerNewUser() {
        String name = TestDataHelper.randomName();
        String email = TestDataHelper.randomEmail();
        String password = TestDataHelper.randomPassword();

        registerPage.register(name, email, password);

        navBar.shouldBeLoggedIn();
    }

    @Test
    @Story("Duplicate Registration")
    @Severity(SeverityLevel.NORMAL)
    @Description("Register with an already existing email should display error")
    @DisplayName("Register with existing email → error message")
    void registerWithExistingEmail() {
        registerPage.register("Admin User", config.adminEmail(), "password123");

        registerPage.getErrorMessage();
    }

    @Test
    @Story("Navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clicking login link on register page should navigate to login page")
    @DisplayName("Click login link → navigate to login page")
    void navigateToLoginPage() {
        registerPage.clickLoginLink();

        webdriver().shouldHave(urlContaining("/login"));
        new LoginPage().shouldBeDisplayed();
    }
}
