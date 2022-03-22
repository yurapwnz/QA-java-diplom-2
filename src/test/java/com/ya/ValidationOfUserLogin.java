package com.ya;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class ValidationOfUserLogin {

    private static UserClient userClient = new UserClient();
    private static User user = User.getRandomUserData();
    private String token;
    private int expectedStatus;
    private String expectedErrorMessage;
    private UserCredentials userCredentials;
    private boolean isSuccessLogin;


    @After
    public void tearDown(){
        if (token != null){
            userClient.delete(token);
        }
    }

    public ValidationOfUserLogin(UserCredentials userCredentials,int expectedStatus,String expectedErrorMessage,boolean isSuccessLogin){
        this.userCredentials = userCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage =expectedErrorMessage;
        this.isSuccessLogin =isSuccessLogin;
    }
    @Parameterized.Parameters
    public static Object[][] getTestData(){
        return new Object[][]{
                {UserCredentials.getUserAuthorizationWithEmailOnly(user),401,"email or password are incorrect",false},
                {UserCredentials.getUserAuthorizationWithPasswordOnly(user),401,"email or password are incorrect",false},
                {UserCredentials.getUserAuthorizationWithOnlyValidEmail(user),401,"email or password are incorrect",false},
                {UserCredentials.getUserAuthorizationWithOnlyValidPassword(user),401,"email or password are incorrect",false},
                {UserCredentials.getUserAuthorizationWithEmpty(user),401,"email or password are incorrect",false}

        };
    }
    @Test
    public void checkValidationOfUserLoginTest(){
        ValidatableResponse response=userClient.create(user);
        token=response.extract().path("accessToken");
        ValidatableResponse errorResponse = new UserClient().login(userCredentials);
        int statusCode = errorResponse.extract().statusCode();
        assertEquals("Status code is incorrect", expectedStatus, statusCode);
        String errorMessage = errorResponse.extract().path("message");
        assertEquals("Message is incorrect", expectedErrorMessage, errorMessage);
        assertFalse("User is login",isSuccessLogin);
    }

}
