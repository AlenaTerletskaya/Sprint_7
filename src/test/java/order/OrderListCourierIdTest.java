package order;

import clients.CourierClient;
import clients.OrderClient;
import data.CourierGenerator;
import data.OrderGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import models.order.orderlist.OrderForList;
import models.order.orderlist.OrderList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;

// Класс с тестами по получению списка заказов по id курьера
public class OrderListCourierIdTest {
    private CourierClient courierClient;
    int courierId;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient(); // Создаем новый объект для отправки запросов по действиям с курьером
        orderClient = new OrderClient(); // Создаем новый объект для отправки запросов по действиям с заказом
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }

    @Test
    @Tag("Positive")
    @DisplayName("Request an order list by courier ID returns all active orders")
    @Description("Проверяет, что при запросе списка заказов по id курьера " +
            "в ответе возвращаются все активные заказы этого курьера")
    public void orderListByCourierId_returnActiveOrders() {
        courierId = CourierGenerator.createCourierAndGetId(courierClient); // Создаем курьера и получаем его id

        int orderId = OrderGenerator.createOrderAndGetId(orderClient); // Создаем заказ и получаем его id
        orderClient.acceptOrder(orderId, courierId); // Принимаем заказ

        // Получаем список заказов по id курьера
        ValidatableResponse responseOrderList = orderClient.getOrderListByCourierId(courierId);

        // Проверям статус-код
        int statusCode = responseOrderList.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_OK, SC_OK, statusCode);

        // Получаем содержимое ответа в виде объекта orderList
        OrderList orderList = responseOrderList.extract().body().as(OrderList.class);
        OrderForList[] orders = orderList.getOrders(); // Получаем список заказов из ответа

        // Проверяем количество заказов в ответе
        int actualNumberOfOrders = orders.length;
        int expectedNumberOfOrders = 1;
        Assert.assertEquals("Number of orsers in list shoul be equal: " + expectedNumberOfOrders,
                expectedNumberOfOrders, actualNumberOfOrders);

        // Проверяем, соответствует ли id курьера в ответе действительному id курьера.
        int actualCourierId = orders[0].getCourierId();
        Assert.assertEquals("Courier id shoul be equal: " + courierId,
                courierId, actualCourierId);

        // Проверяем, соответствует ли id заказа в ответе действительному id заказа.
        int actualOrderId = orders[0].getId();
        Assert.assertEquals("Order id shoul be equal: " + orderId,
                orderId, actualOrderId);

        // Проверяем статус активного заказа
        int actualStatus = orders[0].getStatus();
        int expectedStatus = 1;
        Assert.assertEquals("Order status shoul be equal: " + expectedStatus,
                expectedStatus, actualStatus);

        orderClient.finishOrder(orderId); // Завершаем заказ
    }

    @Test
    @Tag("Positive")
    @DisplayName("Request an order list by courier ID returns all finished orders")
    @Description("Проверяет, что при запросе списка заказов по id курьера " +
            " в ответе возвращаются все завершенные заказы этого курьера")
    public void orderListByCourierId_returnFinishedOrders() {
        courierId = CourierGenerator.createCourierAndGetId(courierClient); // Создаем курьера и получаем его id

        int orderId = OrderGenerator.createOrderAndGetId(orderClient); // Создаем заказ и получаем его id
        orderClient.acceptOrder(orderId, courierId); // Принимаем заказ
        orderClient.finishOrder(orderId); // Завершаем заказ

        // Получаем список заказов по id курьера
        ValidatableResponse responseOrderList = orderClient.getOrderListByCourierId(courierId);

        // Проверям статус-код
        int statusCode = responseOrderList.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_OK, SC_OK, statusCode);

        // Получаем содержимое ответа в виде объекта orderList
        OrderList orderList = responseOrderList.extract().body().as(OrderList.class);
        OrderForList[] orders = orderList.getOrders(); // Получаем список заказов из ответа

        // Проверяем количество заказов в ответе
        int actualNumberOfOrders = orders.length;
        int expectedNumberOfOrders = 1;
        Assert.assertEquals("Number of orsers in list shoul be equal: " + expectedNumberOfOrders,
                expectedNumberOfOrders, actualNumberOfOrders);

        // Проверяем, соответствует ли id курьера в ответе действительному id курьера.
        int actualCourierId = orders[0].getCourierId();
        Assert.assertEquals("Courier id shoul be equal: " + courierId,
                courierId, actualCourierId);

        // Проверяем, соответствует ли id заказа в ответе действительному id заказа.
        int actualOrderId = orders[0].getId();
        Assert.assertEquals("Order id shoul be equal: " + orderId,
                orderId, actualOrderId);

        // Проверяем статус завершенного заказа
        int actualStatus = orders[0].getStatus();
        int expectedStatus = 2;
        Assert.assertEquals("Order status shoul be equal: " + expectedStatus,
                expectedStatus, actualStatus);
    }
}
