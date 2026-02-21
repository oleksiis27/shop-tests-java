package com.shop.api;

import com.shop.config.AppConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthApi {

    private static final String BASE_URL = AppConfig.get().baseUrl();

    @Step("Register user with email: {email}")
    public Response register(String email, String password, String name) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(Map.of("email", email, "password", password, "name", name))
                .when()
                .post("/api/auth/register");
    }

    @Step("Login with email: {email}")
    public Response login(String email, String password) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(Map.of("email", email, "password", password))
                .when()
                .post("/api/auth/login");
    }

    @Step("Get current user profile")
    public Response getMe(String token) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/auth/me");
    }

    @Step("Get current user profile without token")
    public Response getMeWithoutToken() {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .when()
                .get("/api/auth/me");
    }

    @Step("Get current user profile with invalid token")
    public Response getMeWithInvalidToken() {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer invalid-token-12345")
                .when()
                .get("/api/auth/me");
    }
}
