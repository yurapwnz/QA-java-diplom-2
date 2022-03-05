package com.ya;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class UserLoginTest {
    private User user;
    private UserClient userClient;
    private String token;


    @Before
    public void setUp(){
        user = User.getRandomUser();
        userClient = new UserClient();
        ValidatableResponse response=userClient.create(user);
        token=response.extract().path("accessToken");
    }
    @After
    public void tearDown(){
        if (token != null){
            userClient.delete(token);
        }
    }
    @Test
    @Description("Проверка что пользователь может авторизоваться")
    public void checkUserCanBeCreated(){
        ValidatableResponse responseLogin = userClient.login(UserCredentials.from(user));
        boolean isUserLogin = responseLogin.extract().path("success");
        int statusCode = responseLogin.extract().statusCode();
        assertTrue("Courier is not login",isUserLogin);
        assertThat("Status code is incorrect", statusCode, equalTo(200));
    }
}
