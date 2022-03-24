package com.ya;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;


public class OrderClient extends RestAssuredClient {

    private static final String ORDER_PATH = "orders";

    @Step("Create a order")
    public ValidatableResponse createOrder(IngredientsHashes ingredients, String token) {
        return given()
                .spec(getBaseSpec())
                .body(ingredients)
                .auth().oauth2(token.substring(7))
                .when()
                .post(ORDER_PATH)
                .then();
    }
    @Step("Create a order without auth")
    public ValidatableResponse createOrderWithoutAuth(IngredientsHashes ingredients) {
        return given()
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }
    @Step("Get user orders")
    public ValidatableResponse getUserOrders(String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.substring(7))
                .when()
                .get(ORDER_PATH)
                .then();
    }
    @Step("Get user orders")
    public ValidatableResponse getUserOrdersNonAuth() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

}
