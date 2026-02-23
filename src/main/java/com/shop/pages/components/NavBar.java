package com.shop.pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class NavBar {

    private final SelenideElement cartLink = $("a[href='/cart']");
    private final SelenideElement ordersLink = $("a[href='/orders']");
    private final SelenideElement adminLink = $("a[href='/admin']");
    private final SelenideElement loginLink = $("a[href='/login']");
    private final SelenideElement registerLink = $("a[href='/register']");
    private final SelenideElement logoutButton = $("nav button");
    private final SelenideElement userName = $("nav span.text-sm");

    @Step("Click Cart link in navbar")
    public void clickCart() {
        cartLink.shouldBe(visible).click();
    }

    @Step("Click Orders link in navbar")
    public void clickOrders() {
        ordersLink.shouldBe(visible).click();
    }

    @Step("Click Admin link in navbar")
    public void clickAdmin() {
        adminLink.shouldBe(visible).click();
    }

    @Step("Click Login link in navbar")
    public void clickLogin() {
        loginLink.shouldBe(visible).click();
    }

    @Step("Click Register link in navbar")
    public void clickRegister() {
        registerLink.shouldBe(visible).click();
    }

    @Step("Click Logout button in navbar")
    public void clickLogout() {
        logoutButton.shouldBe(visible).click();
    }

    @Step("Get logged-in user name from navbar")
    public String getUserName() {
        return userName.shouldBe(visible).getText();
    }

    @Step("Verify user is logged in (Logout button visible)")
    public void shouldBeLoggedIn() {
        logoutButton.shouldBe(visible);
    }

    @Step("Verify Login link is visible")
    public void loginLinkShouldBeVisible() {
        loginLink.shouldBe(visible);
    }

    @Step("Verify Register link is visible")
    public void registerLinkShouldBeVisible() {
        registerLink.shouldBe(visible);
    }

    @Step("Verify Logout button is visible")
    public void logoutButtonShouldBeVisible() {
        logoutButton.shouldBe(visible);
    }

    @Step("Verify Cart link is visible")
    public void cartLinkShouldBeVisible() {
        cartLink.shouldBe(visible);
    }
}
