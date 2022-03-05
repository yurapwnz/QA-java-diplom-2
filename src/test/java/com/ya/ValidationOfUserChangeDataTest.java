package com.ya;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ValidationOfUserChangeDataTest {
    private User user;
    private UserClient userClient;
    private String token;
    private User userData;


    @Before
    public void setUp() {
        user = User.getRandomUser();
        userData = User.getRandomUser();
        userClient = new UserClient();
        ValidatableResponse response = userClient.create(user);
        token = response.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (token != null) {
            userClient.delete(token);
        }
    }
    @Test
    public void checkChangeDataValidationWithNonAuth(){
        ValidatableResponse responseChangeData = userClient.changeNonAuth(userData);
        int statusCode = responseChangeData.extract().statusCode();
        boolean isUserChanged = responseChangeData.extract().path("success");
        String errorMessage = responseChangeData.extract().path("message");

        assertFalse("Courier is changed", isUserChanged);
        assertThat("Status code is incorrect", statusCode, equalTo(401));
        assertEquals("Message is incorrect","You should be authorised",errorMessage);
    }

}
