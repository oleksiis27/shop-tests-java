package com.shop.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CartPage {

    private final SelenideElement heading = $("h1");
    private final ElementsCollection cartItems = $$("div.bg-white.p-4.rounded-lg");
    private final SelenideElement totalPrice = $("p.text-2xl.font-bold");
    private final SelenideElement checkoutButton = $x("//button[text()='Checkout']");
    private final SelenideElement clearCartButton = $x("//button[text()='Clear Cart']");
    private final SelenideElement emptyCartMessage = $x("//p[text()='Your cart is empty.']");

    @Step("Open cart page")
    public CartPage openPage() {
        open("/cart");
        heading.shouldHave(text("Shopping Cart"));
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
        return this;
    }

    @Step("Get cart item count")
    public int getItemCount() {
        return cartItems.size();
    }

    @Step("Get total price text")
    public String getTotal() {
        return totalPrice.shouldBe(visible).getText();
    }

    @Step("Click increase quantity for item at index {index}")
    public void increaseQuantity(int index) {
        cartItems.get(index).$x(".//button[text()='+']").click();
    }

    @Step("Click decrease quantity for item at index {index}")
    public void decreaseQuantity(int index) {
        cartItems.get(index).$x(".//button[text()='-']").click();
    }

    @Step("Remove item at index {index}")
    public void removeItem(int index) {
        cartItems.get(index).$x(".//button[text()='Remove']").click();
    }

    @Step("Click Clear Cart button")
    public void clearCart() {
        clearCartButton.click();
    }

    @Step("Click Checkout button")
    public void checkout() {
        checkoutButton.click();
    }

    @Step("Verify cart has items")
    public void shouldHaveItems() {
        cartItems.shouldHave(sizeGreaterThan(0));
    }

    @Step("Verify cart is empty")
    public void shouldBeEmpty() {
        emptyCartMessage.shouldBe(visible);
    }
}
