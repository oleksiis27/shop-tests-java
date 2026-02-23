package com.shop.tests.api;

import com.shop.api.CartApi;
import com.shop.api.ProductApi;
import com.shop.helpers.AuthHelper;
import com.shop.helpers.TestDataHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@Epic("Shop API")
@Feature("Cart")
public class CartTests {

    private static final CartApi cartApi = new CartApi();
    private static final ProductApi productApi = new ProductApi();

    private static int testProductId;
    private static int testProductId2;
    private String token;

    @BeforeAll
    static void createTestProducts() {
        String adminToken = AuthHelper.getAdminToken();
        Map<String, Object> product = new HashMap<>(TestDataHelper.randomProduct(1));
        product.put("stock", 9999);
        testProductId = productApi.createProduct(adminToken, product)
                .then().statusCode(201)
                .extract().path("id");

        Map<String, Object> product2 = new HashMap<>(TestDataHelper.randomProduct(1));
        product2.put("stock", 9999);
        testProductId2 = productApi.createProduct(adminToken, product2)
                .then().statusCode(201)
                .extract().path("id");
    }

    @BeforeEach
    void setUp() {
        token = AuthHelper.registerAndGetToken();
    }

    @Test
    @Story("Add to Cart")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Add a product to the cart and verify response")
    @DisplayName("Add item to cart → 201")
    void addItemToCart() {
        cartApi.addItem(token, testProductId, 2)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("product_id", equalTo(testProductId))
                .body("quantity", equalTo(2))
                .body("product.id", equalTo(testProductId))
                .body("product.name", not(emptyOrNullString()));
    }

    @Test
    @Story("View Cart")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Get cart after adding an item and verify contents and total")
    @DisplayName("Get cart → items contain added product, total correct")
    void getCartWithItem() {
        cartApi.addItem(token, testProductId, 2)
                .then().statusCode(201);

        cartApi.getCart(token)
                .then()
                .statusCode(200)
                .body("items", hasSize(1))
                .body("items[0].product_id", equalTo(testProductId))
                .body("items[0].quantity", equalTo(2))
                .body("total", greaterThan(0f));
    }

    @Test
    @Story("Add to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adding the same product again should increase quantity")
    @DisplayName("Add same product again → quantity increases")
    void addSameProductIncreasesQuantity() {
        cartApi.addItem(token, testProductId, 2)
                .then().statusCode(201);

        cartApi.addItem(token, testProductId, 3)
                .then().statusCode(201);

        cartApi.getCart(token)
                .then()
                .statusCode(200)
                .body("items", hasSize(1))
                .body("items[0].quantity", equalTo(5));
    }

    @Test
    @Story("Update Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Update cart item quantity")
    @DisplayName("Update item quantity → 200")
    void updateItemQuantity() {
        int itemId = cartApi.addItem(token, testProductId, 2)
                .then().statusCode(201)
                .extract().path("id");

        cartApi.updateItem(token, itemId, 5)
                .then()
                .statusCode(200)
                .body("quantity", equalTo(5));
    }

    @Test
    @Story("Remove from Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Delete a single item from the cart")
    @DisplayName("Delete cart item → 204")
    void deleteCartItem() {
        int itemId = cartApi.addItem(token, testProductId, 2)
                .then().statusCode(201)
                .extract().path("id");

        cartApi.deleteItem(token, itemId)
                .then()
                .statusCode(204);

        cartApi.getCart(token)
                .then()
                .statusCode(200)
                .body("items", empty());
    }

    @Test
    @Story("Remove from Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clear entire cart and verify it's empty")
    @DisplayName("Clear cart → 204, cart empty after")
    void clearCart() {
        cartApi.addItem(token, testProductId, 1).then().statusCode(201);
        cartApi.addItem(token, testProductId2, 1).then().statusCode(201);

        cartApi.clearCart(token)
                .then()
                .statusCode(204);

        cartApi.getCart(token)
                .then()
                .statusCode(200)
                .body("items", empty())
                .body("total", equalTo(0f));
    }

    @Test
    @Story("Add to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adding item without auth should return 403")
    @DisplayName("Add item without auth → 403")
    void addItemWithoutAuth() {
        cartApi.addItemWithoutAuth(testProductId, 1)
                .then()
                .statusCode(403);
    }
}
