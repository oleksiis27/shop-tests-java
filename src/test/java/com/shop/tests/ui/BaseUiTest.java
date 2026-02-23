package com.shop.tests.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.shop.config.AppConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public abstract class BaseUiTest {

    protected static final AppConfig config = AppConfig.get();

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

    @AfterEach
    void tearDown() {
        Selenide.executeJavaScript("window.localStorage.clear()");
        Selenide.clearBrowserCookies();
    }
}
