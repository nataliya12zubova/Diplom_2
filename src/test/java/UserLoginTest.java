import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserClient;
import org.example.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserLoginTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private String bearerToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandom();
    }

    @After
    public void tearDown() {
        UserClient.delete(bearerToken);
    }

    @Test
    @DisplayName("логин под существующим пользователем")
    @Description("успешный запрос возвращает success: true")
    public void userCanLogIn() {
        userClient.create(user);
        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        accessToken = response.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        int statusCodeSuccessfulLogin = response.extract().statusCode();
        assertThat("User access token is incorrect", bearerToken, is(not("")));
        assertThat(statusCodeSuccessfulLogin, equalTo(200));
    }
}
