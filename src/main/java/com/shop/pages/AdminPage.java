package com.shop.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AdminPage {

    private final SelenideElement heading = $("h1");
    private final SelenideElement ordersTab = $x("//button[text()='Orders']");
    private final SelenideElement productsTab = $x("//button[text()='Products']");
    private final ElementsCollection orderCards = $$("div.bg-white.p-6.rounded-lg.shadow");
    private final SelenideElement addProductButton = $x("//button[text()='Add Product']");
    private final ElementsCollection productRows = $$("table tbody tr");

    // Add product form fields
    private final SelenideElement productNameInput = $("form input[name='name']");
    private final SelenideElement productPriceInput = $("form input[name='price']");
    private final SelenideElement productStockInput = $("form input[name='stock']");
    private final SelenideElement productCategorySelect = $("form select[name='category_id']");
    private final SelenideElement productDescriptionInput = $("form textarea[name='description']");
    private final SelenideElement productImageInput = $("form input[name='image_url']");
    private final SelenideElement createButton = $x("//button[text()='Create']");

    @Step("Open admin page")
    public AdminPage openPage() {
        open("/admin");
        heading.shouldHave(text("Admin Panel"));
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
        return this;
    }

    @Step("Switch to Orders tab")
    public void switchToOrdersTab() {
        ordersTab.shouldBe(visible).click();
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 300))");
    }

    @Step("Switch to Products tab")
    public void switchToProductsTab() {
        productsTab.shouldBe(visible).click();
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 300))");
    }

    @Step("Get order card count")
    public int getOrderCount() {
        return orderCards.size();
    }

    @Step("Get product row count")
    public int getProductCount() {
        return productRows.size();
    }

    @Step("Update order status at index {index} to {status}")
    public void updateOrderStatus(int index, String status) {
        orderCards.get(index).$x(".//button[text()='" + status + "']").click();
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
    }

    @Step("Get order status at index {index}")
    public String getOrderStatus(int index) {
        return orderCards.get(index).$("span.rounded-full").getText();
    }

    @Step("Add product: {name}")
    public void addProduct(String name, String description, double price, int stock, String category, String imageUrl) {
        addProductButton.shouldBe(visible).click();
        productNameInput.shouldBe(visible).setValue(name);
        productDescriptionInput.setValue(description);
        productPriceInput.setValue(String.valueOf(price));
        productStockInput.setValue(String.valueOf(stock));
        productCategorySelect.selectOptionContainingText(category);
        productImageInput.setValue(imageUrl);
        createButton.shouldBe(visible).click();
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
    }

    @Step("Delete product at index {index}")
    public void deleteProduct(int index) {
        productRows.get(index).$x(".//button[text()='Delete']").click();
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
    }

    @Step("Verify admin page is displayed")
    public void shouldBeDisplayed() {
        heading.shouldHave(text("Admin Panel"));
    }

    @Step("Verify orders are displayed")
    public void ordersShouldBeDisplayed() {
        orderCards.shouldHave(sizeGreaterThan(0));
    }

    @Step("Verify products are displayed")
    public void productsShouldBeDisplayed() {
        productRows.shouldHave(sizeGreaterThan(0));
    }
}
