import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.example.Ingredients;
import org.example.OrderClient;
import org.example.User;
import org.example.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class OrderCreateTest {
    private User user;
    private UserClient userClient;
    private Ingredients ingredients;
    public OrderClient orderClient;

    private String accessToken;
    private String bearerToken;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        ingredients = Ingredients.getRandomBurger();
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        UserClient.delete(bearerToken);
    }

    @Test
    @Description("Создание заказа. Зарегистрированный пользователь")
    public void orderCanBeCreatedAuthUser (){
        ValidatableResponse userResponse = userClient.create(user);
        accessToken = userResponse.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        ValidatableResponse orderResponse = orderClient.create(bearerToken, ingredients);
        int statusCode = orderResponse.extract().statusCode();
        boolean orderCreated = orderResponse.extract().path("success");
        int orderNumber = orderResponse.extract().path("order.number");
        assertThat ("Status code is not correct", statusCode, equalTo(200));
        assertThat("The order has not been created", orderCreated, is(true));
        assertThat("The order number is missing", orderNumber, is(not(0)));
    }

    @Test
    @Description ("Создание заказа. Не зарегистрированный пользователь")
    public void orderCanBeCreatedNonAuthUser (){
        bearerToken = "";
        ValidatableResponse orderResponse = orderClient.create(bearerToken,ingredients);
        int statusCode = orderResponse.extract().statusCode();
        boolean orderCreated = orderResponse.extract().path("success");
        int orderNumber = orderResponse.extract().path("order.number");
        assertThat ("Status code is not correct", statusCode, equalTo(200));
        assertThat("The order has not been created", orderCreated, is(true));
        assertThat("The order number is missing", orderNumber, is(not(0)));
    }

    @Test
    @Description ("Создание заказа без ингредиентов")
    public void orderCantBeCreatedWithOutIngredients (){
        ValidatableResponse userResponse = userClient.create(user);
        accessToken = userResponse.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        ValidatableResponse orderResponse = orderClient.create(bearerToken, Ingredients.getNullIngredients());
        int statusCode = orderResponse.extract().statusCode();
        boolean orderNotCreated = orderResponse.extract().path("success");
        String errorMessage = orderResponse.extract().path("message");
        assertThat ("Status code is not correct", statusCode, equalTo(400));
        assertThat("The order has not been created", orderNotCreated, is(false));
        assertEquals("The error message is not correct", "Ingredient ids must be provided", errorMessage);
    }

    @Test
    @Description ("Создание заказа с невалидными ингридиентами")
    public void orderCantBeCreatedWithIncorrectIngredients (){
        ValidatableResponse userResponse = userClient.create(user);
        accessToken = userResponse.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        ValidatableResponse orderResponse = orderClient.create(bearerToken,Ingredients.getIncorrectIngredients());
        int statusCode = orderResponse.extract().statusCode();
        assertThat ("Status code is not correct", statusCode, equalTo(500));
    }
}
