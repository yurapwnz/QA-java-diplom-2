package com.ya;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CreateOrderTest {
    List<String> ingredients = new ArrayList<>();
    private UserClient userClient;
    private IngredientClient ingredientClient;
    private OrderClient orderClient;
    private String token;

    @Before
    public void setUp() {
        User user = User.getRandomUser();
        userClient = new UserClient();
        ingredientClient = new IngredientClient();
        orderClient = new OrderClient();
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
    @Description ("Создание заказа. Зарегистрированный пользователь")
    public void checkCreateOrderWithAuth(){
        ingredients = ingredientClient.getIngredients().extract().path("data._id");
        IngredientsHashes orderIngredients = new IngredientsHashes(ingredients.get(0));
        ValidatableResponse response =orderClient.createOrder(orderIngredients,token);
        int statusCode = response.extract().statusCode();
        boolean isOrderCreationSuccess = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");

        assertThat("Status code is incorrect", statusCode, equalTo(200));
        assertTrue("Order didn't create", isOrderCreationSuccess);
        assertThat("Order number is null", orderNumber, notNullValue());
    }
    @Test
    @Description ("Создание заказа. Не зарегистрированный пользователь")
    public void checkCreateOrderWithoutAuth(){
        ingredients = ingredientClient.getIngredients().extract().path("data._id");
        IngredientsHashes orderIngredients = new IngredientsHashes(ingredients.get(0));
        ValidatableResponse response =orderClient.createOrderWithoutAuth(orderIngredients);
        int statusCode = response.extract().statusCode();
        boolean isOrderCreationSuccess = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");

        assertThat("Status code is incorrect", statusCode, equalTo(200));
        assertTrue("Order didn't create", isOrderCreationSuccess);
        assertThat("Order number is null", orderNumber, notNullValue());
    }
    @Test
    @Description("Создание заказа без ингредиентов")
    public void checkCreateOrderWithoutIngredients(){
        IngredientsHashes orderIngredients = new IngredientsHashes("");
        ValidatableResponse response =orderClient.createOrder(orderIngredients,token);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertThat("Status code is incorrect", statusCode, equalTo(400));
        assertThat("Message is incorrect",errorMessage,equalTo("Ingredient ids must be provided"));
    }
    @Test
    @Description ("Создание заказа с невалидными ингридиентами")
    public void checkCreateOrderWithIncorrectHashIngredient(){
        IngredientsHashes orderIngredients = new IngredientsHashes("asdfg");
        ValidatableResponse response =orderClient.createOrder(orderIngredients,token);
        int statusCode = response.extract().statusCode();

        assertThat("Status code is incorrect", statusCode, equalTo(500));
    }








}
