import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserClient;
import org.example.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class UserCreateTest {

    private User user;
    private UserClient userClient;
    private String accessToken;
    private String bearerToken;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        UserClient.delete(bearerToken);
    }

    @Test
    @DisplayName("Пользователя можно создать")
    @Description("успешный запрос возвращает success: true")
    public void checkUserCanBeCreated() {
        ValidatableResponse response = userClient.create(user);
        int statusCode = response.extract().statusCode();
        boolean isUserCreated = response.extract().path("success");
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        assertThat("User is not created", isUserCreated, is(true));
        assertThat("Status code is incorrect", statusCode, equalTo(200));
        assertThat("User access token is incorrect", accessToken, is(not("")));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых пользователей")
    @Description("если создать пользователя с логином, который уже есть - возвращается ошибка")
    public void duplicateUserCannotBeCreated() {
        userClient.create(user);
        ValidatableResponse response = userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        assertThat("Status code is incorrect", statusCode, equalTo(403));
        assertThat("Duplicate user has been created", errorMessage, equalTo("User already exists"));
    }
}
