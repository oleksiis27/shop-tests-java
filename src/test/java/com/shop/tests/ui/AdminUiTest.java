package com.shop.tests.ui;

import com.shop.pages.AdminPage;
import com.shop.pages.LoginPage;
import com.shop.pages.components.NavBar;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Shop UI")
@Feature("Admin Panel")
public class AdminUiTest extends BaseUiTest {

    private final LoginPage loginPage = new LoginPage();
    private final AdminPage adminPage = new AdminPage();
    private final NavBar navBar = new NavBar();

    @BeforeEach
    void setUp() {
        loginPage.openPage();
        loginPage.login(config.adminEmail(), config.adminPassword());
        navBar.shouldBeLoggedIn();
        adminPage.openPage();
    }

    @Test
    @Story("Admin Orders")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Admin can see all orders in the admin panel")
    @DisplayName("Admin opens orders tab: all orders visible")
    void adminSeesAllOrders() {
        adminPage.switchToOrdersTab();

        adminPage.ordersShouldBeDisplayed();
    }

    @Test
    @Story("Admin Orders")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Admin can change order status from pending to confirmed")
    @DisplayName("Admin updates order status: status changes to confirmed")
    void adminUpdatesOrderStatus() {
        adminPage.switchToOrdersTab();

        adminPage.updateOrderStatus(0, "confirmed");

        String status = adminPage.getOrderStatus(0);
        assertThat(status.toLowerCase()).contains("confirmed");
    }

    @Test
    @Story("Admin Products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Admin can create a new product via the admin panel")
    @DisplayName("Admin adds product: product appears in table")
    void adminCreatesProduct() {
        adminPage.switchToProductsTab();
        int initialCount = adminPage.getProductCount();

        adminPage.addProduct(
                "Test Product UI",
                "Test description for UI product",
                29.99,
                50,
                "Electronics",
                "https://example.com/test.jpg"
        );

        assertThat(adminPage.getProductCount()).isGreaterThan(initialCount);
    }

    @Test
    @Story("Admin Products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Admin can delete a product from the admin panel")
    @DisplayName("Admin deletes product: product disappears from table")
    void adminDeletesProduct() {
        adminPage.switchToProductsTab();
        int initialCount = adminPage.getProductCount();

        adminPage.deleteProduct(initialCount - 1);

        assertThat(adminPage.getProductCount()).isLessThan(initialCount);
    }
}