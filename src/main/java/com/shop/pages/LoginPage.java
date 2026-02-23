package com.shop.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final SelenideElement emailInput = $("input[name='email']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement loginButton = $("button[type='submit']");
    private final SelenideElement registerLink = $("a[href='/register']");
    private final SelenideElement errorMessage = $("p.text-red-600");
    private final SelenideElement heading = $("h1");

    @Step("Open login page")
    public LoginPage openPage() {
        open("/login");
        heading.shouldHave(text("Login"));
        // Wait for React AuthProvider to finish initializing
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
        return this;
    }

    @Step("Login with email: {email}")
    public void login(String email, String password) {
        emailInput.shouldBe(visible).setValue(email);
        passwordInput.shouldBe(visible).setValue(password);
        loginButton.shouldBe(visible).click();
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        return errorMessage.shouldBe(visible).getText();
    }

    @Step("Click Register link")
    public void clickRegisterLink() {
        registerLink.click();
    }

    @Step("Verify login page is displayed")
    public void shouldBeDisplayed() {
        heading.shouldHave(text("Login"));
        emailInput.shouldBe(visible);
    }
}
