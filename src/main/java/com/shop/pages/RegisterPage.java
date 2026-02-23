package com.shop.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class RegisterPage {

    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement emailInput = $("input[name='email']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement registerButton = $("button[type='submit']");
    private final SelenideElement loginLink = $("a[href='/login']");
    private final SelenideElement errorMessage = $("p.text-red-600");
    private final SelenideElement heading = $("h1");

    @Step("Open register page")
    public RegisterPage openPage() {
        open("/register");
        heading.shouldHave(text("Register"));
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
        return this;
    }

    @Step("Register with name: {name}, email: {email}")
    public void register(String name, String email, String password) {
        nameInput.shouldBe(visible).setValue(name);
        emailInput.shouldBe(visible).setValue(email);
        passwordInput.shouldBe(visible).setValue(password);
        registerButton.shouldBe(visible).click();
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        return errorMessage.shouldBe(visible).getText();
    }

    @Step("Click Login link")
    public void clickLoginLink() {
        loginLink.click();
    }

    @Step("Verify register page is displayed")
    public void shouldBeDisplayed() {
        heading.shouldHave(text("Register"));
        nameInput.shouldBe(visible);
    }
}
