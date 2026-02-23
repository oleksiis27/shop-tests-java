package com.shop.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class HomePage {

    private final SelenideElement searchInput = $("input[name='search']");
    private final SelenideElement searchButton = $("form button[type='submit']");
    private final SelenideElement categorySelect = $$("select").first();
    private final SelenideElement sortSelect = $$("select").last();
    private final ElementsCollection productCards = $$(".grid a");
    private final SelenideElement nextPageButton = $x("//button[text()='Next']");
    private final SelenideElement prevPageButton = $x("//button[text()='Previous']");

    @Step("Open home page")
    public HomePage openPage() {
        open("/");
        $("nav").shouldBe(visible);
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
        return this;
    }

    @Step("Search for product: {query}")
    public void searchProduct(String query) {
        searchInput.shouldBe(visible).setValue(query);
        searchButton.click();
    }

    @Step("Select category: {name}")
    public void selectCategory(String name) {
        categorySelect.shouldBe(visible).selectOptionContainingText(name);
    }

    @Step("Select sort option: {option}")
    public void selectSort(String option) {
        sortSelect.shouldBe(visible).selectOptionContainingText(option);
    }

    @Step("Get product cards")
    public ElementsCollection getProductCards() {
        return productCards;
    }

    @Step("Click on product at index {index}")
    public void clickProduct(int index) {
        productCards.get(index).click();
    }

    @Step("Click Next page button")
    public void clickNextPage() {
        nextPageButton.shouldBe(visible).click();
    }

    @Step("Click Previous page button")
    public void clickPrevPage() {
        prevPageButton.shouldBe(visible).click();
    }

    @Step("Verify products are displayed")
    public void productsShouldBeDisplayed() {
        productCards.shouldHave(sizeGreaterThan(0));
    }
}
