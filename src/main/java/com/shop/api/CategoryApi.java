package com.shop.api;

import com.shop.config.AppConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CategoryApi {

    private static final String BASE_URL = AppConfig.get().baseUrl();

    @Step("Get all categories")
    public Response getCategories() {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .when()
                .get("/api/categories");
    }
}
