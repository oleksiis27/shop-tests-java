package com.shop.api;

import com.shop.config.AppConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class CartApi {

    private static final String BASE_URL = AppConfig.get().baseUrl();

    private RequestSpecification authSpec(String token) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }

    @Step("Get cart")
    public Response getCart(String token) {
        return authSpec(token)
                .when()
                .get("/api/cart");
    }

    @Step("Add item to cart: product_id={productId}, quantity={quantity}")
    public Response addItem(String token, int productId, int quantity) {
        return authSpec(token)
                .body(Map.of("product_id", productId, "quantity", quantity))
                .when()
                .post("/api/cart/items");
    }

    @Step("Add item to cart without auth")
    public Response addItemWithoutAuth(int productId, int quantity) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(Map.of("product_id", productId, "quantity", quantity))
                .when()
                .post("/api/cart/items");
    }

    @Step("Update cart item ID: {itemId}, quantity: {quantity}")
    public Response updateItem(String token, int itemId, int quantity) {
        return authSpec(token)
                .body(Map.of("quantity", quantity))
                .when()
                .put("/api/cart/items/" + itemId);
    }

    @Step("Delete cart item ID: {itemId}")
    public Response deleteItem(String token, int itemId) {
        return authSpec(token)
                .when()
                .delete("/api/cart/items/" + itemId);
    }

    @Step("Clear cart")
    public Response clearCart(String token) {
        return authSpec(token)
                .when()
                .delete("/api/cart");
    }
}