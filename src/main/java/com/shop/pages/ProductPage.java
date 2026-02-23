package com.shop.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProductPage {

    private final SelenideElement productName = $("h1");
    private final SelenideElement price = $("p.text-3xl.font-bold");
    private final SelenideElement description = $("p.text-gray-600");
    private final SelenideElement quantityInput = $("input[type='number']");
    private final SelenideElement addToCartButton = $x("//button[text()='Add to Cart']");
    private final SelenideElement successMessage = $("p.text-green-600");

    @Step("Open product page with id: {id}")
    public ProductPage openPage(int id) {
        open("/products/" + id);
        productName.shouldBe(visible);
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
        return this;
    }

    @Step("Get product name")
    public String getProductName() {
        return productName.shouldBe(visible).getText();
    }

    @Step("Get product price")
    public String getPrice() {
        return price.shouldBe(visible).getText();
    }

    @Step("Get product description")
    public String getDescription() {
        return description.shouldBe(visible).getText();
    }

    @Step("Set quantity to {quantity}")
    public void setQuantity(int quantity) {
        quantityInput.shouldBe(visible).clear();
        quantityInput.setValue(String.valueOf(quantity));
    }

    @Step("Click Add to Cart button")
    public void addToCart() {
        addToCartButton.shouldBe(visible).click();
    }

    @Step("Get success message")
    public String getSuccessMessage() {
        return successMessage.shouldBe(visible).getText();
    }

    @Step("Verify product details are displayed")
    public void shouldBeDisplayed() {
        productName.shouldBe(visible);
        price.shouldBe(visible);
    }
}
