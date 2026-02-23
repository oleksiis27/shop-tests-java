package com.shop.tests.api;

import com.shop.api.AuthApi;
import com.shop.config.AppConfig;
import com.shop.helpers.TestDataHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@Epic("Shop API")
@Feature("Authentication")
public class AuthTests {

    private static final AuthApi authApi = new AuthApi();
    private static final AppConfig config = AppConfig.get();

    private static String userEmail;
    private static String userPassword;
    private static String userName;

    @BeforeAll
    static void setUp() {
        userEmail = TestDataHelper.randomEmail();
        userPassword = TestDataHelper.randomPassword();
        userName = TestDataHelper.randomName();
    }

    @Test
    @Story("Registration")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Register a new user and verify response fields")
    @DisplayName("Register new user → 201")
    void registerNewUser() {
        authApi.register(userEmail, userPassword, userName)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("email", equalTo(userEmail))
                .body("name", equalTo(userName))
                .body("role", equalTo("user"))
                .body("created_at", notNullValue());
    }

    @Test
    @Story("Registration")
    @Severity(SeverityLevel.NORMAL)
    @Description("Attempt to register with an already existing email should return 409")
    @DisplayName("Register duplicate email → 409")
    void registerDuplicateEmail() {
        String email = TestDataHelper.randomEmail();
        authApi.register(email, "password123", "First User");

        authApi.register(email, "password456", "Second User")
                .then()
                .statusCode(409);
    }

    @Test
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Login with valid credentials and verify access_token is returned")
    @DisplayName("Login with valid credentials → 200")
    void loginWithValidCredentials() {
        authApi.login(config.adminEmail(), config.adminPassword())
                .then()
                .statusCode(200)
                .body("access_token", not(emptyOrNullString()))
                .body("token_type", equalTo("bearer"));
    }

    @Test
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Login with wrong password should return 401")
    @DisplayName("Login with wrong password → 401")
    void loginWithWrongPassword() {
        authApi.login(config.adminEmail(), "wrongpassword")
                .then()
                .statusCode(401);
    }

    @Test
    @Story("User Profile")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Get current user profile with valid token")
    @DisplayName("GET /me with valid token → 200")
    void getMeWithValidToken() {
        String token = authApi.login(config.adminEmail(), config.adminPassword())
                .then().statusCode(200)
                .extract().path("access_token");

        authApi.getMe(token)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("email", equalTo(config.adminEmail()))
                .body("role", equalTo("admin"));
    }

    @Test
    @Story("User Profile")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /me without Authorization header should return 403")
    @DisplayName("GET /me without token → 403")
    void getMeWithoutToken() {
        authApi.getMeWithoutToken()
                .then()
                .statusCode(403);
    }

    @Test
    @Story("User Profile")
    @Severity(SeverityLevel.NORMAL)
    @Description("GET /me with invalid token should return 401")
    @DisplayName("GET /me with invalid token → 401")
    void getMeWithInvalidToken() {
        authApi.getMeWithInvalidToken()
                .then()
                .statusCode(401);
    }
}
