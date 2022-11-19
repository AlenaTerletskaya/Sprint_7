package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.courier.Courier;
import models.courier.CourierDeletion;
import models.courier.Credentials;

import static io.restassured.RestAssured.given;

// Класс-клиент для отправки запросов к эндпойнтам по действиям с курьером.
public class CourierClient extends Client {

    private static final String PATH_CREATE = "api/v1/courier";
    private static final String PATH_LOGIN = "/api/v1/courier/login";

    // Метод отправляет запрос на создание курьера и возвращает ответ.
    @Step("Send POST request to create a courier to api/v1/courier")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getRequestSpec())
                .body(courier)
                .when()
                .post(PATH_CREATE)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на логин курьера и возвращает ответ.
    @Step("Send POST request for authorization of the courier to /api/v1/courier/login")
    public ValidatableResponse login(Credentials credentials) {
        return given()
                .spec(getRequestSpec())
                .body(credentials)
                .when()
                .post(PATH_LOGIN)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на удаление курьера и возвращает ответ.
    @Step("Send DELETE request to remove the courier to api/v1/courier/:id")
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getRequestSpec())
                .body(CourierDeletion.by(id))
                .when()
                .delete(PATH_CREATE + "/" + id)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на удаление курьера без id и возвращает ответ.
    @Step("Send DELETE request to remove the courier with null id to api/v1/courier/")
    public ValidatableResponse deleteNullId() {

        return given()
                .spec(getRequestSpec())
                .body("{\"id\": null}")
                .when()
                .delete(PATH_CREATE + "/")
                .then()
                .spec(getResponseSpec());
    }
}
