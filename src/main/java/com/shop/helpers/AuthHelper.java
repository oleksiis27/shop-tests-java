package com.shop.helpers;

import com.shop.api.AuthApi;
import com.shop.config.AppConfig;
import io.qameta.allure.Step;

public class AuthHelper {

    private static final AuthApi authApi = new AuthApi();

    @Step("Get admin token")
    public static String getAdminToken() {
        return authApi.login(AppConfig.get().adminEmail(), AppConfig.get().adminPassword())
                .then().statusCode(200)
                .extract().path("access_token");
    }

    @Step("Get default user token")
    public static String getUserToken() {
        return authApi.login(AppConfig.get().userEmail(), AppConfig.get().userPassword())
                .then().statusCode(200)
                .extract().path("access_token");
    }

    @Step("Register new user and get token")
    public static String registerAndGetToken() {
        String email = TestDataHelper.randomEmail();
        String password = TestDataHelper.randomPassword();
        String name = TestDataHelper.randomName();

        authApi.register(email, password, name)
                .then().statusCode(201);

        return authApi.login(email, password)
                .then().statusCode(200)
                .extract().path("access_token");
    }
}
