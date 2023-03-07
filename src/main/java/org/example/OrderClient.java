package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {
    private static final String ORDER_PATH = "api/orders";
    private static final String INGREDIENTS_PATH = "api/ingredients";

    @Step("Создание заказа")
    public ValidatableResponse create (String token, Ingredients ingredients){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step ("Список заказов")
    public ValidatableResponse orderInfo (){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH + "/all")
                .then();
    }

    @Step ("Список заказов пользователя")
    public ValidatableResponse userOrderInfo (String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .when()
                .get(ORDER_PATH)
                .then();
    }
    @Step  ("Список ингредиентов")
    public ValidatableResponse getRandomBurger() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .statusCode(200);
    }
}

