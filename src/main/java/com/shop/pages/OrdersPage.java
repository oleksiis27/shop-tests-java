package com.shop.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class OrdersPage {

    private final ElementsCollection orders = $$("div.bg-white.p-6.rounded-lg.shadow");

    @Step("Open orders page")
    public OrdersPage openPage() {
        open("/orders");
        $("h1").shouldHave(text("My Orders"));
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
        return this;
    }

    @Step("Get order count")
    public int getOrderCount() {
        return orders.size();
    }

    @Step("Get order status at index {index}")
    public String getOrderStatus(int index) {
        return orders.get(index).$("span.rounded-full").getText();
    }

    @Step("Get order total at index {index}")
    public String getOrderTotal(int index) {
        return orders.get(index).$("p.text-lg.font-bold").getText();
    }

    @Step("Verify orders are displayed")
    public void shouldHaveOrders() {
        orders.shouldHave(sizeGreaterThan(0));
    }

    @Step("Verify orders page is displayed")
    public void shouldBeDisplayed() {
        $("h1").shouldHave(text("My Orders"));
    }
}
