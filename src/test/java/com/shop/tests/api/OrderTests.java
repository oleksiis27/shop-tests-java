package com.shop.tests.api;

import com.shop.api.CartApi;
import com.shop.api.OrderApi;
import com.shop.helpers.AuthHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@Epic("Shop API")
@Feature("Orders")
public class OrderTests {

    private static final CartApi cartApi = new CartApi();
    private static final OrderApi orderApi = new OrderApi();

    private String userToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        userToken = AuthHelper.registerAndGetToken();
        adminToken = AuthHelper.getAdminToken();
    }

    private int createOrderWithProduct() {
        cartApi.addItem(userToken, 1, 2).then().statusCode(201);

        return orderApi.createOrder(userToken)
                .then().statusCode(201)
                .extract().path("id");
    }

    @Test
    @Story("Create Order")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Create order from non-empty cart and verify response")
    @DisplayName("Create order from cart → 201, status=pending")
    void createOrderFromCart() {
        cartApi.addItem(userToken, 1, 2).then().statusCode(201);

        orderApi.createOrder(userToken)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("status", equalTo("pending"))
                .body("total", greaterThan(0f))
                .body("items", hasSize(1))
                .body("items[0].product_id", equalTo(1))
                .body("items[0].quantity", equalTo(2))
                .body("items[0].price", greaterThan(0f));
    }

    @Test
    @Story("Create Order")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Creating an order from an empty cart should return 400")
    @DisplayName("Create order from empty cart → 400")
    void createOrderFromEmptyCart() {
        orderApi.createOrder(userToken)
                .then()
                .statusCode(400);
    }

    @Test
    @Story("Create Order")
    @Severity(SeverityLevel.CRITICAL)
    @Description("After creating an order the cart should be empty")
    @DisplayName("Cart is empty after order creation")
    void cartEmptyAfterOrder() {
        cartApi.addItem(userToken, 1, 1).then().statusCode(201);

        orderApi.createOrder(userToken)
                .then().statusCode(201);

        cartApi.getCart(userToken)
                .then()
                .statusCode(200)
                .body("items", empty())
                .body("total", equalTo(0f));
    }

    @Test
    @Story("View Orders")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Get user orders list and verify it contains the created order")
    @DisplayName("Get orders list → contains created order")
    void getOrdersList() {
        int orderId = createOrderWithProduct();

        orderApi.getOrders(userToken)
                .then()
                .statusCode(200)
                .body("id", hasItem(orderId));
    }

    @Test
    @Story("View Orders")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Get order by ID and verify all fields")
    @DisplayName("Get order by ID → 200")
    void getOrderById() {
        int orderId = createOrderWithProduct();

        orderApi.getOrder(userToken, orderId)
                .then()
                .statusCode(200)
                .body("id", equalTo(orderId))
                .body("status", equalTo("pending"))
                .body("total", greaterThan(0f))
                .body("user_id", notNullValue())
                .body("created_at", notNullValue())
                .body("items", not(empty()));
    }

    @Test
    @Story("View Orders")
    @Severity(SeverityLevel.CRITICAL)
    @Description("User should not be able to access another user's order")
    @DisplayName("Get another user's order → 404")
    void getOtherUserOrder() {
        int orderId = createOrderWithProduct();

        String anotherUserToken = AuthHelper.registerAndGetToken();

        orderApi.getOrder(anotherUserToken, orderId)
                .then()
                .statusCode(404);
    }

    @Test
    @Story("Admin Orders")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Admin can get all orders")
    @DisplayName("Admin: get all orders → 200")
    void adminGetAllOrders() {
        createOrderWithProduct();

        orderApi.getAdminOrders(adminToken)
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    @Story("Admin Orders")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Admin updates order status from pending to confirmed")
    @DisplayName("Admin: pending → confirmed → 200")
    void adminUpdateStatusToConfirmed() {
        int orderId = createOrderWithProduct();

        orderApi.updateOrderStatus(adminToken, orderId, "confirmed")
                .then()
                .statusCode(200)
                .body("status", equalTo("confirmed"));
    }

    @Test
    @Story("Admin Orders")
    @Severity(SeverityLevel.NORMAL)
    @Description("Invalid status transition should return 400")
    @DisplayName("Admin: invalid status transition → 400")
    void adminInvalidStatusTransition() {
        int orderId = createOrderWithProduct();

        orderApi.updateOrderStatus(adminToken, orderId, "delivered")
                .then()
                .statusCode(400);
    }

    @Test
    @Story("Admin Orders")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Regular user cannot update order status")
    @DisplayName("User: update order status → 403")
    void userCannotUpdateStatus() {
        int orderId = createOrderWithProduct();

        orderApi.updateOrderStatus(userToken, orderId, "confirmed")
                .then()
                .statusCode(403);
    }
}