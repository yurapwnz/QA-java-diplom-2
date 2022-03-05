package com.ya;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class UserCreateTest {
    private User user;
    private UserClient userClient;
    private String token;


    @Before
    public void setUp(){
        user = User.getRandomUser();
        userClient = new UserClient();
    }
    @After
    public void tearDown(){
       if (token != null){
           userClient.delete(token);
       }
    }

    @Test
    @Description("Проверка регистрации пользователя с валидными данными")
    public void checkUserCanBeCreated(){
        ValidatableResponse response = userClient.create(user);
        ValidatableResponse responseLogin =userClient.login(UserCredentials.from(user));
        int statusCode = response.extract().statusCode();
        boolean isUserCreated = response.extract().path("success");
        token=response.extract().path("accessToken");
        boolean isUserLogin =responseLogin.extract().path("success");

        assertTrue("Courier is not created", isUserCreated);
        assertThat("Status code is incorrect", statusCode, equalTo(200));
        assertTrue("Courier is not login",isUserLogin);
    }
    @Test
    @Description("Проверка, что нельзя создать дубликать пользователя")
    public void duplicateUserCannotBeCreated(){
        userClient.create(user);
        ValidatableResponse response = userClient.create(user);
        int statusCode = response.extract().statusCode();
        boolean isUserCreated = response.extract().path("success");
        String errorMessage = response.extract().path("message");

        assertFalse("Courier is created", isUserCreated);
        assertThat("Status code is incorrect", statusCode, equalTo(403));
        assertEquals("Message is incorrect","User already exists",errorMessage);

    }

}
