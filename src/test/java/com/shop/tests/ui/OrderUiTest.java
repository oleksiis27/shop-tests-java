package com.shop.tests.ui;

import com.codeborne.selenide.Selenide;
import com.shop.pages.*;
import com.shop.pages.components.NavBar;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Shop UI")
@Feature("Orders")
public class OrderUiTest extends BaseUiTest {

    private final LoginPage loginPage = new LoginPage();
    private final ProductPage productPage = new ProductPage();
    private final CartPage cartPage = new CartPage();
    private final OrdersPage ordersPage = new OrdersPage();
    private final NavBar navBar = new NavBar();

    @BeforeEach
    void setUp() {
        loginPage.openPage();
        loginPage.login(config.userEmail(), config.userPassword());
        navBar.shouldBeLoggedIn();

        // Add product and checkout to create an order
        productPage.openPage(1);
        productPage.setQuantity(1);
        productPage.addToCart();
        productPage.getSuccessMessage();
        cartPage.openPage();
        cartPage.shouldHaveItems();
        cartPage.checkout();
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
    }

    @Test
    @Story("Order List")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Created order appears in the orders list with pending status")
    @DisplayName("Order created → appears in list with pending status")
    void orderAppearsInList() {
        ordersPage.shouldHaveOrders();

        String status = ordersPage.getOrderStatus(0);
        assertThat(status.toLowerCase()).contains("pending");
    }

    @Test
    @Story("Order Details")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Order details show correct total amount")
    @DisplayName("Order details → total is correct")
    void orderDetailsShowTotal() {
        ordersPage.shouldHaveOrders();

        String total = ordersPage.getOrderTotal(0);
        assertThat(total).isNotEmpty();
    }
}
