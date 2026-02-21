package com.shop.api;

import com.shop.config.AppConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderApi {

    private static final String BASE_URL = AppConfig.get().baseUrl();

    private RequestSpecification authSpec(String token) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }

    @Step("Create order from cart")
    public Response createOrder(String token) {
        return authSpec(token)
                .when()
                .post("/api/orders");
    }

    @Step("Get user orders")
    public Response getOrders(String token) {
        return authSpec(token)
                .when()
                .get("/api/orders");
    }

    @Step("Get order by ID: {orderId}")
    public Response getOrder(String token, int orderId) {
        return authSpec(token)
                .when()
                .get("/api/orders/" + orderId);
    }

    @Step("Admin: get all orders")
    public Response getAdminOrders(String token) {
        return authSpec(token)
                .when()
                .get("/api/admin/orders");
    }

    @Step("Admin: update order {orderId} status to {status}")
    public Response updateOrderStatus(String token, int orderId, String status) {
        return authSpec(token)
                .body(Map.of("status", status))
                .when()
                .put("/api/admin/orders/" + orderId + "/status");
    }
}
