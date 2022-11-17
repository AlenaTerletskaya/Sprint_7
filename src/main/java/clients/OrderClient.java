package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.order.Order;
import models.order.OrderCancel;
import models.order.OrderFinish;

import static io.restassured.RestAssured.given;

// Класс-клиент для отправки запросов к эндпойнтам по действиям с заказом.
public class OrderClient extends Client{

    // Пусть для создания заказа
    private static final String PATH_CREATE = "/api/v1/orders";
    // ПУть для отмены заказа
    private static final String PATH_CANCEL = "/api/v1/orders/cancel";
    // Путь для завершения заказа
    private static final String PATH_FINISH = "/api/v1/orders/finish/";
    // Путь для получения заказа по его номеру
    private static final String PATH_GET_ORDER = "/api/v1/orders/track";
    // Путь для принятия заказа
    private static final String PATH_ACCEPT_ORDER = "/api/v1/orders/accept/";
    // Путь для получения списка заказов
    private static final String PATH_GET_ORDER_LIST = "/api/v1/orders";



    // Метод отправляет запрос на создание заказа и возвращает ответ.
    @Step("Send POST request to create an order to /api/v1/orders")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getRequestSpec())
                .body(order)
                .when()
                .post(PATH_CREATE)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на отмену заказа и возвращает ответ.
    @Step("Send PUT request to cancel an order to /api/v1/orders/cancel")
    public ValidatableResponse cancelOrder(int track) {
        return given()
                .spec(getRequestSpec())
                .body(OrderCancel.by(track))
                .when()
                .queryParam("track", track)
                .put(PATH_CANCEL)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на завершение заказа и возвращает ответ.
    @Step("Send PUT request to finish an order to /api/v1/orders/finish/:id")
    public ValidatableResponse finishOrder(int id) {
        return given()
                .spec(getRequestSpec())
                .body(OrderFinish.by(id))
                .when()
                .put(PATH_FINISH + id)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на получение заказа по его номеру и возвращает ответ.
    @Step("Send GET request to receive an order by its track to /api/v1/orders/track")
    public ValidatableResponse getOrderByTrack(int track) {
        return given()
                .spec(getRequestSpec())
                .when()
                .queryParam("t", track)
                .get(PATH_GET_ORDER)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на принятие заказа и возвращает ответ.
    @Step("Send PUT request to accept an order to /api/v1/orders/accept/:id")
    public ValidatableResponse acceptOrder(int id, int courierId) {
        return given()
                .spec(getRequestSpec())
                .when()
                .queryParam("courierId", courierId)
                .put(PATH_ACCEPT_ORDER + id)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на получение списка заказов по id курьера и возвращает ответ.
    @Step("Send GET request to receive a list of orders by courier id to /api/v1/orders")
    public ValidatableResponse getOrderListByCourierId(int courierId) {
        return given()
                .spec(getRequestSpec())
                .when()
                .queryParam("courierId", courierId)
                .get(PATH_GET_ORDER_LIST)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на получение списка заказов без id курьера и возвращает ответ.
    @Step("Send GET request to receive a list of orders without a courier id to /api/v1/orders")
    public ValidatableResponse getOrderListNoCourierId() {
        return given()
                .spec(getRequestSpec())
                .when()
                .get(PATH_GET_ORDER_LIST)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на принятие заказа без id курьера и возвращает ответ.
    @Step("Send PUT request to accept an order without a courier id to /api/v1/orders/accept/:id")
    public ValidatableResponse acceptOrderNoCourierId(int id) {
        return given()
                .spec(getRequestSpec())
                .when()
                .put(PATH_ACCEPT_ORDER + id)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на принятие заказа без id заказа и возвращает ответ.
    @Step("Send PUT request to accept an order to /api/v1/orders/accept/")
    public ValidatableResponse acceptOrderNoOrderId(int courierId) {
        return given()
                .spec(getRequestSpec())
                .when()
                .queryParam("courierId", courierId)
                .put(PATH_ACCEPT_ORDER)
                .then()
                .spec(getResponseSpec());
    }

    // Метод отправляет запрос на получение заказа без его номера и возвращает ответ.
    @Step("Send GET request to receive an order without its track to /api/v1/orders/track")
    public ValidatableResponse getOrderWithoutTrack() {
        return given()
                .spec(getRequestSpec())
                .when()
                .get(PATH_GET_ORDER)
                .then()
                .spec(getResponseSpec());
    }

}
