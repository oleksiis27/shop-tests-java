package com.shop.tests.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.shop.api.ProductApi;
import com.shop.config.AppConfig;
import com.shop.helpers.AuthHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public abstract class BaseUiTest {

    protected static final AppConfig config = AppConfig.get();
    private static final ProductApi productApi = new ProductApi();

    @BeforeAll
    static void setUpSelenide() {
        Configuration.baseUrl = config.uiUrl();
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 30000;

        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true));
    }

    /**
     * Wait for React app to finish AuthProvider initialization.
     * After open(), React mounts and AuthProvider triggers a re-render
     * when loading changes from true to false — this can wipe form values
     * if we interact too early.
     */
    protected static void waitForReactReady() {
        $("nav").shouldBe(visible);
        Selenide.executeJavaScript(
                "return new Promise(resolve => setTimeout(resolve, 500))");
    }

    /**
     * Create a product with guaranteed high stock via API.
     * Use this instead of hardcoding product IDs to avoid
     * stock depletion issues when API tests run first.
     */
    protected static int createTestProduct() {
        String adminToken = AuthHelper.getAdminToken();
        return productApi.createProduct(adminToken, Map.of(
                "name", "UI Test Product",
                "description", "Product created for UI tests",
                "price", 19.99,
                "stock", 9999,
                "category_id", 1,
                "image_url", "https://example.com/test.jpg"
        )).then().statusCode(201).extract().path("id");
    }

    @AfterEach
    void tearDown() {
        Selenide.executeJavaScript("window.localStorage.clear()");
        Selenide.clearBrowserCookies();
    }
}