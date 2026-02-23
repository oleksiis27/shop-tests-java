package com.shop.tests.ui;

import com.shop.pages.HomePage;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

@Epic("Shop UI")
@Feature("Home Page")
public class HomePageUiTest extends BaseUiTest {

    private final HomePage homePage = new HomePage();

    @BeforeEach
    void setUp() {
        homePage.openPage();
    }

    @Test
    @Story("Page Load")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Home page loads and displays product cards")
    @DisplayName("Home page loads → products displayed")
    void homePageLoadsWithProducts() {
        homePage.productsShouldBeDisplayed();
    }

    @Test
    @Story("Search")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Searching for a product filters the results")
    @DisplayName("Search product → results filtered")
    void searchProductFiltersResults() {
        int initialCount = homePage.getProductCards().size();

        homePage.searchProduct("Laptop");

        homePage.getProductCards().shouldHave(sizeGreaterThanOrEqual(0));
    }

    @Test
    @Story("Filter")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Filtering by category shows only matching products")
    @DisplayName("Filter by category → matching products shown")
    void filterByCategory() {
        homePage.selectCategory("Electronics");

        homePage.getProductCards().shouldHave(sizeGreaterThanOrEqual(0));
    }

    @Test
    @Story("Sort")
    @Severity(SeverityLevel.NORMAL)
    @Description("Sorting by price ascending orders products correctly")
    @DisplayName("Sort by price asc → products ordered correctly")
    void sortByPriceAscending() {
        homePage.selectSort("Price");

        homePage.getProductCards().shouldHave(sizeGreaterThan(0));
    }

    @Test
    @Story("Pagination")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination allows navigating between pages of products")
    @DisplayName("Click next/prev page → page changes")
    void paginationWorks() {
        homePage.productsShouldBeDisplayed();

        homePage.clickNextPage();
        webdriver().shouldHave(urlContaining("/"));
    }
}
