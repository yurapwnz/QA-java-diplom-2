package com.ya;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {

    private static final String USER_PATCH ="auth/";
    @Step("Create user")
    public ValidatableResponse create(User user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .post(USER_PATCH+"register")
                .then();
    }
    @Step("Login of a user")
    public ValidatableResponse login(UserCredentials credentials){
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_PATCH+"login")
                .then();
    }
    @Step("Delete client")
    public ValidatableResponse delete(String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.substring(7))
                .when()
                .delete(USER_PATCH+"user")
                .then();
    }
    @Step("Change of user data")
    public ValidatableResponse change(User userData,String token){
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .auth().oauth2(token.substring(7))
                .when()
                .patch(USER_PATCH+"user")
                .then();
    }
    @Step("Change NonAuth of user data")
    public ValidatableResponse changeNonAuth(User userData){
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .patch(USER_PATCH+"user")
                .then();
    }

}
