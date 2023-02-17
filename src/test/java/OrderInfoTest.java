import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.example.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class OrderInfoTest {
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
    @Description("Получение списка заказов")
    public void orderInfoCanBeGet (){
        bearerToken = "";
        ValidatableResponse orderInfo = orderClient.orderInfo();
        int statusCode = orderInfo.extract().statusCode();
        boolean orderInfoGet = orderInfo.extract().path("success");
        List<Map<String, Object>> ordersList = orderInfo.extract().path("orders");

        assertThat ("Status code is not correct", statusCode, equalTo(200));
        assertThat("Information about orders has not been received", orderInfoGet, is(true));
        assertThat("Orders list empty", ordersList, is(not(0)));
    }

    @Test
    @Description("Получение списка заказов авторизованного пользователя")
    public void orderUserInfoCanBeGetAuthUser (){
        userClient.create(user);
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        accessToken = login.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        ValidatableResponse orderInfo = orderClient.userOrderInfo(bearerToken);
        int statusCode = orderInfo.extract().statusCode();
        boolean orderCreated = orderInfo.extract().path("success");
        List<Map<String, Object>> ordersList = orderInfo.extract().path("orders");
        assertThat ("Status code is not correct", statusCode, equalTo(200));
        assertThat("Information about orders has not been received", orderCreated, is(true));
        assertThat("Orders list empty", ordersList, is(not(0)));
    }

    @Test
    @Description("Получение списка заказов не авторизованного пользователя")
    public void orderUserInfoCantBeGetNonAuthUser (){
        bearerToken = "";
        ValidatableResponse orderInfo = orderClient.userOrderInfo(bearerToken);
        int statusCode = orderInfo.extract().statusCode();
        boolean orderInfoNotGet = orderInfo.extract().path("success");
        String errorMessage = orderInfo.extract().path("message");

        assertThat ("Status code is incorrect", statusCode, equalTo(401));
        assertThat("Information about orders has not been received", orderInfoNotGet, is(false));
        assertEquals("The error message is not correct", "You should be authorised", errorMessage);
    }
}
