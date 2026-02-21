package com.shop.tests.products;

import com.shop.api.ProductApi;
import com.shop.helpers.AuthHelper;
import com.shop.helpers.TestDataHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@Epic("Shop API")
@Feature("Products")
public class ProductTests {

    private static final ProductApi productApi = new ProductApi();

    private static String adminToken;
    private static String userToken;

    @BeforeAll
    static void setUp() {
        adminToken = AuthHelper.getAdminToken();
        userToken = AuthHelper.getUserToken();
    }

    @Test
    @Story("Product List")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Get products list with default pagination")
    @DisplayName("Get products list → 200, items not empty")
    void getProductsList() {
        productApi.getProducts(null)
                .then()
                .statusCode(200)
                .body("items", not(empty()))
                .body("total", greaterThan(0))
                .body("page", equalTo(1))
                .body("limit", greaterThan(0))
                .body("pages", greaterThan(0));
    }

    @Test
    @Story("Product List")
    @Severity(SeverityLevel.NORMAL)
    @Description("Filter products by category and verify all returned items belong to that category")
    @DisplayName("Filter products by category")
    void filterByCategory() {
        int categoryId = 1;

        productApi.getProducts(Map.of("category", categoryId))
                .then()
                .statusCode(200)
                .body("items", not(empty()))
                .body("items.category_id", everyItem(equalTo(categoryId)));
    }

    @Test
    @Story("Product List")
    @Severity(SeverityLevel.NORMAL)
    @Description("Search products by name and verify results contain search term")
    @DisplayName("Search products by name")
    void searchByName() {
        // Get a product name first to use as search term
        String productName = productApi.getProducts(Map.of("limit", 1))
                .then().statusCode(200)
                .extract().path("items[0].name");

        String searchTerm = productName.split(" ")[0];

        productApi.getProducts(Map.of("search", searchTerm))
                .then()
                .statusCode(200)
                .body("items", not(empty()));
    }

    @Test
    @Story("Product List")
    @Severity(SeverityLevel.NORMAL)
    @Description("Sort products by price ascending and verify order")
    @DisplayName("Sort products by price asc")
    void sortByPriceAsc() {
        List<Float> prices = productApi.getProducts(Map.of("sort_by", "price_asc"))
                .then()
                .statusCode(200)
                .extract().path("items.price");

        for (int i = 1; i < prices.size(); i++) {
            assert prices.get(i) >= prices.get(i - 1) :
                    "Prices not in ascending order: " + prices.get(i - 1) + " > " + prices.get(i);
        }
    }

    @Test
    @Story("Product List")
    @Severity(SeverityLevel.NORMAL)
    @Description("Sort products by price descending and verify order")
    @DisplayName("Sort products by price desc")
    void sortByPriceDesc() {
        List<Float> prices = productApi.getProducts(Map.of("sort_by", "price_desc"))
                .then()
                .statusCode(200)
                .extract().path("items.price");

        for (int i = 1; i < prices.size(); i++) {
            assert prices.get(i) <= prices.get(i - 1) :
                    "Prices not in descending order: " + prices.get(i - 1) + " < " + prices.get(i);
        }
    }

    @Test
    @Story("Product Details")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Get product by ID and verify all fields are present")
    @DisplayName("Get product by ID → 200")
    void getProductById() {
        productApi.getProduct(1)
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", not(emptyOrNullString()))
                .body("description", notNullValue())
                .body("price", notNullValue())
                .body("stock", notNullValue())
                .body("category_id", notNullValue())
                .body("category.id", notNullValue())
                .body("category.name", not(emptyOrNullString()))
                .body("created_at", notNullValue());
    }

    @Test
    @Story("Product Details")
    @Severity(SeverityLevel.NORMAL)
    @Description("Get non-existent product should return 404")
    @DisplayName("Get non-existent product → 404")
    void getNonExistentProduct() {
        productApi.getProduct(99999)
                .then()
                .statusCode(404);
    }

    @Test
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Admin creates a new product successfully")
    @DisplayName("Create product (admin) → 201")
    void createProductAsAdmin() {
        Map<String, Object> product = TestDataHelper.randomProduct(1);

        productApi.createProduct(adminToken, product)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(product.get("name")))
                .body("price", notNullValue())
                .body("stock", equalTo(product.get("stock")))
                .body("category_id", equalTo(1));
    }

    @Test
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Regular user cannot create a product")
    @DisplayName("Create product (user) → 403")
    void createProductAsUser() {
        Map<String, Object> product = TestDataHelper.randomProduct(1);

        productApi.createProduct(userToken, product)
                .then()
                .statusCode(403);
    }

    @Test
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Unauthenticated request cannot create a product")
    @DisplayName("Create product without auth → 403")
    void createProductWithoutAuth() {
        Map<String, Object> product = TestDataHelper.randomProduct(1);

        productApi.createProductWithoutAuth(product)
                .then()
                .statusCode(403);
    }

    @Test
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Admin updates a product and verifies changes")
    @DisplayName("Update product (admin) → 200")
    void updateProductAsAdmin() {
        // Create a product to update
        Map<String, Object> product = TestDataHelper.randomProduct(1);
        int productId = productApi.createProduct(adminToken, product)
                .then().statusCode(201)
                .extract().path("id");

        String updatedName = "Updated Product Name";
        productApi.updateProduct(adminToken, productId, Map.of("name", updatedName))
                .then()
                .statusCode(200)
                .body("name", equalTo(updatedName))
                .body("id", equalTo(productId));
    }

    @Test
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Admin deletes a product successfully")
    @DisplayName("Delete product (admin) → 204")
    void deleteProductAsAdmin() {
        // Create a product to delete
        Map<String, Object> product = TestDataHelper.randomProduct(1);
        int productId = productApi.createProduct(adminToken, product)
                .then().statusCode(201)
                .extract().path("id");

        productApi.deleteProduct(adminToken, productId)
                .then()
                .statusCode(204);

        // Verify product is deleted
        productApi.getProduct(productId)
                .then()
                .statusCode(404);
    }

    @Test
    @Story("Product Management")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deleting a non-existent product should return 404")
    @DisplayName("Delete non-existent product → 404")
    void deleteNonExistentProduct() {
        productApi.deleteProduct(adminToken, 99999)
                .then()
                .statusCode(404);
    }
}
