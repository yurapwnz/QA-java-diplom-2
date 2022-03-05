package com.ya;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetOrderListTest {

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
            ValidatableResponse response = userClient.create(user);
            token = response.extract().path("accessToken");
            ingredients = ingredientClient.getIngredients().extract().path("data._id");
            IngredientsHashes orderIngredients = new IngredientsHashes(ingredients.get(0));
            orderClient.createOrder(orderIngredients,token);
        }

        @After
        public void tearDown() {
            if (token != null) {
                userClient.delete(token);
            }
        }
        @Test
        @Description("Получить список заказов")
        public void checkListOfOrderTest(){
            ValidatableResponse responseList = orderClient.getUserOrders(token);
            int statusCode = responseList.extract().statusCode();
            List<Map<String, String>> orders = responseList.extract().path("orders");

            assertThat("Status code is incorrect", statusCode, equalTo(200));
            assertThat("List is empty",orders,hasSize(1));
        }
        @Test
        @Description("Получить список заказов без авторизации")
         public void checkListOfOrderWithNonAuthTest(){
            ValidatableResponse responseList = orderClient.getUserOrdersNonAuth();
            int statusCode = responseList.extract().statusCode();
            String errorMessage = responseList.extract().path("message");

            assertThat("Status code is incorrect", statusCode, equalTo(401));
            assertThat("Message is incorrect", errorMessage, equalTo("You should be authorised"));

    }
    }

