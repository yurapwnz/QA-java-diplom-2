package com.ya;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ValidationOfUserCreateTest {

    private UserClient userClient;
    private int expectedStatus;
    private String expectedErrorMessage;
    private User user;

    @Before
    public void setUp(){
        userClient = new UserClient();
    }

    public ValidationOfUserCreateTest(User user,int expectedStatus,String expectedErrorMessage){
        this.user = user;
        this.expectedStatus =expectedStatus;
        this.expectedErrorMessage =expectedErrorMessage;
    }
    @Parameterized.Parameters
    public static Object[][] getTestData(){
        return new Object[][]{
                {User.getWithoutEmail(),403,"Email, password and name are required fields"},
                {User.getWithoutName(),403,"Email, password and name are required fields"},
                {User.getWithoutPassword(),403,"Email, password and name are required fields"},
                {User.getWithPasswordOnly(),403,"Email, password and name are required fields"},
                {User.getWithEmailOnly(),403,"Email, password and name are required fields"},
                {User.getWithNameOnly(),403,"Email, password and name are required fields"},
                {User.getEmpty(),403,"Email, password and name are required fields"}
        };
    }

    @Test
    public void checkValidationUserOfUserCreateTest(){
        ValidatableResponse response = new UserClient().create(user);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertEquals("Status code is incorrect", expectedStatus, statusCode);
        assertEquals("Message is incorrect", expectedErrorMessage, errorMessage);
    }
}
