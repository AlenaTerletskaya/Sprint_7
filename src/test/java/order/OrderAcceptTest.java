package order;

import clients.CourierClient;
import clients.OrderClient;
import data.CourierGenerator;
import data.OrderGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

// Класс с тестами по принятию заказа
public class OrderAcceptTest {

    private CourierClient courierClient;
    private OrderClient orderClient;
    int courierId;
    int orderId;

    @Before
    public void setUp() {
        courierClient = new CourierClient(); // Создаем новый объект для отправки запросов по действиям с курьером
        orderClient = new OrderClient(); // Создаем новый объект для отправки запросов по действиям с заказом
    }

    @Test
    @Tag("Positive")
    @DisplayName("Courier can accept the order")
    @Description("Проверяет, что курьер может принять заказ. " +
            "Успешный запрос возвращает код 200 и соответствующий текст")
    public void courierCanAcceptOrder() {
        courierId = CourierGenerator.createCourierAndGetId(courierClient); // Создаем курьера и получаем его id

        orderId = OrderGenerator.createOrderAndGetId(orderClient); // Создаем заказ и получаем его id
        ValidatableResponse responseAccept = orderClient.acceptOrder(orderId, courierId); // Принимаем заказ

        // Проверям статус-код
        int statusCode = responseAccept.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_OK, SC_OK, statusCode);

        // Проверяем текст сообщения
        boolean isOrderAccepted = responseAccept.extract().path("ok");
        Assert.assertTrue("Value should be equal: true", isOrderAccepted);

        courierClient.delete(courierId); // Удаляем курьера
        orderClient.finishOrder(orderId); // Завершаем заказ
    }

    @Test
    @Tag("Negative")
    @DisplayName("Request to accept an order with a wrong courier id returns an error")
    @Description("Проверяет, что запрос на принятие заказа с неверным id курьера " +
            "возвращает ошибку 404 и соответствующий текст.")
    public void orderAcceptWrongCourierId() {
        // Создаем заказ и получаем его track и id
        int[] orderTrackAndId = OrderGenerator.createOrderAndGetTrackAndId(orderClient);

        // Принимаем заказ с id курьера = 0
        ValidatableResponse responseAccept = orderClient.acceptOrder(orderTrackAndId[1], 0);

        // Проверям статус-код
        int statusCode = responseAccept.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_NOT_FOUND, SC_NOT_FOUND, statusCode);

        // Проверяем текст сообщения
        String actualMessage = responseAccept.extract().path("message");
        String message = "Курьера с таким id не существует";
        Assert.assertEquals("Message should be equal: " + message, message, actualMessage);

        orderClient.cancelOrder(orderTrackAndId[0]); // Отменяем заказ
    }


    @Test
    @Tag("Negative")
    @DisplayName("Request to accept an order with a wrong order id returns an error")
    @Description("Проверяет, что запрос на принятие заказа с неверным id заказа " +
            "возвращает ошибку 404 и соответствующий текст.")
    public void orderAcceptWrongOrderId() {
        courierId = CourierGenerator.createCourierAndGetId(courierClient); // Создаем курьера и получаем его id

        // Принимаем заказ с id заказа = 0
        ValidatableResponse responseAccept = orderClient.acceptOrder(0, courierId);

        // Проверям статус-код
        int statusCode = responseAccept.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_NOT_FOUND, SC_NOT_FOUND, statusCode);

        // Проверяем текст сообщения
        String actualMessage = responseAccept.extract().path("message");
        String message = "Заказа с таким id не существует";
        Assert.assertEquals("Message should be equal: " + message, message, actualMessage);

        courierClient.delete(courierId); // Удаляем курьера
    }

    @Test
    @Tag("Negative")
    @DisplayName("Request to accept an order without a courier id returns an error")
    @Description("Проверяет, что запрос на принятие заказа без id курьера " +
            "возвращает ошибку 400 и соответствующий текст.")
    public void orderAcceptNoCourierId() {
        // Создаем заказ и получаем его track и id
        int[] orderTrackAndId = OrderGenerator.createOrderAndGetTrackAndId(orderClient);

        // Принимаем заказ без id курьера
        ValidatableResponse responseAccept = orderClient.acceptOrderNoCourierId(orderTrackAndId[1]);

        // Проверям статус-код
        int statusCode = responseAccept.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_BAD_REQUEST, SC_BAD_REQUEST, statusCode);

        // Проверяем текст сообщения
        String actualMessage = responseAccept.extract().path("message");
        String message = "Недостаточно данных для поиска";
        Assert.assertEquals("Message should be equal: " + message, message, actualMessage);

        orderClient.cancelOrder(orderTrackAndId[0]); // Отменяем заказ
    }

    @Test
    @Ignore
    @Tag("Negative")
    @DisplayName("Request to accept an order without an order id returns an error")
    @Description("Проверяет, что запрос на принятие заказа без id заказа " +
            "возвращает ошибку 400 и соответствующий текст.")
    public void orderAcceptNoOrderId() {
        courierId = CourierGenerator.createCourierAndGetId(courierClient); // Создаем курьера и получаем его id

        // Принимаем заказ без id заказа
        ValidatableResponse responseAccept = orderClient.acceptOrderNoOrderId(courierId);

        // Проверям статус-код
        int statusCode = responseAccept.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_BAD_REQUEST, SC_BAD_REQUEST, statusCode);

        // Проверяем текст сообщения
        String actualMessage = responseAccept.extract().path("message");
        String message = "Недостаточно данных для поиска";
        Assert.assertEquals("Message should be equal: " + message, message, actualMessage);

        courierClient.delete(courierId); // Удаляем курьера
    }


}
