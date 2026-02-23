package com.shop.tests.ui;

import com.shop.pages.LoginPage;
import com.shop.pages.ProductPage;
import com.shop.pages.components.NavBar;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Shop UI")
@Feature("Product Page")
public class ProductUiTest extends BaseUiTest {

    private static int testProductId;
    private final ProductPage productPage = new ProductPage();
    private final LoginPage loginPage = new LoginPage();
    private final NavBar navBar = new NavBar();

    @BeforeAll
    static void createProduct() {
        testProductId = createTestProduct();
    }

    @Test
    @Story("Product Details")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Opening a product page shows all product details: name, price, description")
    @DisplayName("Open product page → all details displayed")
    void productPageShowsDetails() {
        productPage.openPage(testProductId);

        productPage.shouldBeDisplayed();
        assertThat(productPage.getProductName()).isNotEmpty();
        assertThat(productPage.getPrice()).isNotEmpty();
    }

    @Test
    @Story("Add to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Logged-in user can add product to cart and sees success message")
    @DisplayName("Add product to cart → success message shown")
    void addProductToCart() {
        loginPage.openPage();
        loginPage.login(config.userEmail(), config.userPassword());
        navBar.shouldBeLoggedIn();

        productPage.openPage(testProductId);
        productPage.setQuantity(1);
        productPage.addToCart();

        String message = productPage.getSuccessMessage();
        assertThat(message).isNotEmpty();
    }

    @Test
    @Story("Add to Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Unauthorized user is redirected to login when trying to add product to cart")
    @DisplayName("Unauthorized add to cart → redirect to login")
    void unauthorizedAddToCartRedirectsToLogin() {
        productPage.openPage(testProductId);
        productPage.addToCart();

        webdriver().shouldHave(urlContaining("/login"));
    }
}
