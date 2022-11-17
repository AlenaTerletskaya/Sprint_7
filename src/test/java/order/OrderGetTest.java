package order;

import clients.OrderClient;
import data.OrderGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import models.order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

// Класс с тестами по получению заказа
public class OrderGetTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient(); // Создаем новый объект для отправки запросов по действиям с заказом
    }

    @Test
    @Tag("Positive")
    @DisplayName("Get an order by its track")
    @Description("Проверяет, что можно получить заказ по его трек-номеру. " +
            "Успешный запрос возвращает код 200 и содержание заказа")
    public void getOrderByTrack() {
        Order order = OrderGenerator.getNewOrder(); // Генерируем новый заказ с данными
        ValidatableResponse responseOrder = orderClient.createOrder(order); // Создаем заказ
        int track = responseOrder.extract().path("track"); // Получаем трек-номер заказа

        // Получаем заказ по его трек-номеру
        ValidatableResponse responseGetOrder = orderClient.getOrderByTrack(track);

        // Проверям статус-код
        int statusCode = responseGetOrder.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_OK, SC_OK, statusCode);

        // Проверяем, что id полученного заказа не null.
        Number orderId = responseGetOrder.extract().path("order.id");
        Assert.assertNotNull("Order id shoul not be null", orderId);

        // Проверяем, что трек полученного заказа совпадает с переданным в запросе
        int actualTrack = responseGetOrder.extract().path("order.track");
        Assert.assertEquals("Track shoul be equal: " + track,
                track, actualTrack);

        // Проверяем, что фамилия заказчика в полученном заказе совпадает с переданным в запросе
        String actualLastName = responseGetOrder.extract().path("order.lastName");
        String expectedLastName = order.getLastName();
        Assert.assertEquals("Last name shoul be equal: " + actualLastName,
                actualLastName, expectedLastName);

        orderClient.cancelOrder(track); // Отменяем заказ
    }

    @Test
    @Tag("Negative")
    @DisplayName("Request to receive an order without its track returns an error")
    @Description("Проверяет, что запрос на получение заказа без его трек-номера " +
            "возвращает ошибку 400 и соответствующий текст")
    public void getOrderWithoutTrack() {

        // Получаем заказ без его трек-номера
        ValidatableResponse responseGetOrder = orderClient.getOrderWithoutTrack();

        // Проверям статус-код
        int statusCode = responseGetOrder.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_BAD_REQUEST, SC_BAD_REQUEST, statusCode);

        // Проверяем текст сообщения
        String actualMessage = responseGetOrder.extract().path("message");
        String message = "Недостаточно данных для поиска";
        Assert.assertEquals("Message should be equal: " + message, message, actualMessage);
    }

    @Test
    @Tag("Negative")
    @DisplayName("Request to receive an order with a non-existent track returns an error")
    @Description("Проверяет, что запрос на получение заказа с несуществующим номером " +
            "возвращает ошибку 404 и соответствующий текст")
    public void getOrderWithNonExistentTrack() {

        // Получаем заказ по несуществующему трек-номеру = 0
        ValidatableResponse responseGetOrder = orderClient.getOrderByTrack(0);

        // Проверям статус-код
        int statusCode = responseGetOrder.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_NOT_FOUND, SC_NOT_FOUND, statusCode);

        // Проверяем текст сообщения
        String actualMessage = responseGetOrder.extract().path("message");
        String message = "Заказ не найден";
        Assert.assertEquals("Message should be equal: " + message, message, actualMessage);
    }
}
