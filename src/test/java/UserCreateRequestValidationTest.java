import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserClient;
import org.example.UserCredentials;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class UserCreateRequestValidationTest {

    private final User user;
    private final int expectedStatus;
    private final String expectedErrorMessage;

    private String bearerToken;
    private String accessToken;

    public UserCreateRequestValidationTest(User user, int expectedStatus, String expectedErrorMessage) {
        this.user = user;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {User.getUserWithoutName(), 403, "Email, password and name are required fields"},
                {User.getUserWithoutPassword(), 403, "Email, password and name are required fields"},
                {User.getUserWithoutEmail(), 403, "Email, password and name are required fields"}
        };
    }
    @After
    public void tearDown() {
        UserClient.delete(bearerToken);
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
    @Description("Если нет одного из полей, вернётся код ответа 403 Forbidden")
    public void invalidRequestIsNotAllowed() {
        ValidatableResponse response = new UserClient().create(user);
        bearerToken = "";
        String actualMessage = response.extract().path("message");
        int code = response.extract().statusCode();
        if (response.extract().statusCode() == 200) {
            System.out.println("Пользователь не должен был создаться!");
            UserClient userClient = new UserClient();
            bearerToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken").toString().split(" ")[1];
            userClient.delete(bearerToken);
        }
        assertEquals (expectedErrorMessage, actualMessage);
        assertEquals (expectedStatus, code);
    }
}

