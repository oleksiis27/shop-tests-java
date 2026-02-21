package com.shop.api;

import com.shop.config.AppConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProductApi {

    private static final String BASE_URL = AppConfig.get().baseUrl();

    private RequestSpecification baseSpec() {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL);
    }

    private RequestSpecification authSpec(String token) {
        return baseSpec()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }

    @Step("Get products list")
    public Response getProducts(Map<String, Object> queryParams) {
        RequestSpecification spec = baseSpec();
        if (queryParams != null) {
            spec.queryParams(queryParams);
        }
        return spec.when().get("/api/products");
    }

    @Step("Get product by ID: {id}")
    public Response getProduct(int id) {
        return baseSpec()
                .when()
                .get("/api/products/" + id);
    }

    @Step("Create product as admin")
    public Response createProduct(String token, Map<String, Object> body) {
        return authSpec(token)
                .body(body)
                .when()
                .post("/api/products");
    }

    @Step("Create product without auth")
    public Response createProductWithoutAuth(Map<String, Object> body) {
        return baseSpec()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/products");
    }

    @Step("Update product ID: {id}")
    public Response updateProduct(String token, int id, Map<String, Object> body) {
        return authSpec(token)
                .body(body)
                .when()
                .put("/api/products/" + id);
    }

    @Step("Delete product ID: {id}")
    public Response deleteProduct(String token, int id) {
        return authSpec(token)
                .when()
                .delete("/api/products/" + id);
    }
}
