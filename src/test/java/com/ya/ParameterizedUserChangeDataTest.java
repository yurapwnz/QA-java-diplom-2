package com.ya;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class ParameterizedUserChangeDataTest {

    private static UserClient userClient = new UserClient();
    private static User user = User.getRandomUserData();
    private static User userData;
    private String token;
    private int expectedStatus;
    private boolean isSuccessChange;



    @After
    public void tearDown() {
        if (token != null) {
            userClient.delete(token);
        }

    }

    public ParameterizedUserChangeDataTest(User userData, int expectedStatus, boolean isSuccessChange) {
        this.userData = userData;
        this.expectedStatus = expectedStatus;
        this.isSuccessChange = isSuccessChange;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData(){
        return new Object[][]{
                {User.getRandomUserData(),200,true},
                {User.getWithoutEmail(),200,true},
                {User.getWithoutName(),200,true},
                {User.getWithoutPassword(),200,true},
                {User.getWithPasswordOnly(),200,true},
                {User.getWithEmailOnly(),200,true},
                {User.getWithNameOnly(),200,true}
        };

    }
    @Test
    public void checkChangeUserData(){
        ValidatableResponse response = userClient.create(user);
        token = response.extract().path("accessToken");
        ValidatableResponse responseChangeData = userClient.change(userData,token);
        int statusCode = response.extract().statusCode();
        boolean isUserChanged = responseChangeData.extract().path("success");

        assertEquals("Courier is not changed",isSuccessChange,isUserChanged);
        assertEquals("Status code is incorrect", expectedStatus,statusCode);
    }
}

