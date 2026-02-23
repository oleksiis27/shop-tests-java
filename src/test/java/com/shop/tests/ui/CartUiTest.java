package com.shop.tests.ui;

import com.codeborne.selenide.Selenide;
import com.shop.pages.CartPage;
import com.shop.pages.LoginPage;
import com.shop.pages.ProductPage;
import com.shop.pages.components.NavBar;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Shop UI")
@Feature("Cart")
public class CartUiTest extends BaseUiTest {

    private final LoginPage loginPage = new LoginPage();
    private final ProductPage productPage = new ProductPage();
    private final CartPage cartPage = new CartPage();
    private final NavBar navBar = new NavBar();

    @BeforeEach
    void setUp() {
        loginPage.openPage();
        loginPage.login(config.userEmail(), config.userPassword());
        navBar.shouldBeLoggedIn();
    }

    private void addProductToCart() {
        productPage.openPage(1);
        productPage.setQuantity(1);
        productPage.addToCart();
        productPage.getSuccessMessage();
    }

    @Test
    @Story("View Cart")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Product added to cart is visible in the cart page")
    @DisplayName("Add product → visible in cart")
    void addedProductVisibleInCart() {
        addProductToCart();
        cartPage.openPage();

        cartPage.shouldHaveItems();
    }

    @Test
    @Story("Update Quantity")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Increasing item quantity updates the total")
    @DisplayName("Change quantity → total updates")
    void changeQuantityUpdatesTotal() {
        addProductToCart();
        cartPage.openPage();
        cartPage.shouldHaveItems();

        String initialTotal = cartPage.getTotal();
        cartPage.increaseQuantity(0);
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");

        String updatedTotal = cartPage.getTotal();
        assertThat(updatedTotal).isNotEqualTo(initialTotal);
    }

    @Test
    @Story("Remove Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Removing an item from cart makes it disappear")
    @DisplayName("Remove item → disappears from cart")
    void removeItemFromCart() {
        addProductToCart();
        cartPage.openPage();

        cartPage.shouldHaveItems();
        cartPage.removeItem(0);

        cartPage.shouldBeEmpty();
    }

    @Test
    @Story("Clear Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clearing the cart removes all items")
    @DisplayName("Clear cart → cart is empty")
    void clearCartRemovesAllItems() {
        addProductToCart();
        cartPage.openPage();
        cartPage.shouldHaveItems();

        cartPage.clearCart();
        cartPage.shouldBeEmpty();
    }

    @Test
    @Story("Checkout")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checkout creates an order and redirects to orders page")
    @DisplayName("Checkout → order created, redirect to orders")
    void checkoutCreatesOrder() {
        addProductToCart();
        cartPage.openPage();

        cartPage.shouldHaveItems();
        cartPage.checkout();
        webdriver().shouldHave(urlContaining("/orders"));
    }
}