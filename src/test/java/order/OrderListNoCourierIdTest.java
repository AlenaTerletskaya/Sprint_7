package order;

import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import models.order.orderlist.OrderForList;
import models.order.orderlist.OrderList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;

// Класс с тестами по получению списка заказов без id курьера
public class OrderListNoCourierIdTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient(); // Создаем новый объект для отправки запросов по действиям с заказом
    }

    @Test
    @Tag("Positive")
    @DisplayName("Request an order list without a courier ID returns orders")
    @Description("Проверяет, что при запросе списка заказов без id курьера в тело ответа возвращаются заказы")
    public void orderListNoCourierId_returnOrders() {

        // Получаем список заказов без id курьера
        ValidatableResponse responseOrderList = orderClient.getOrderListNoCourierId();

        // Проверям статус-код
        int statusCode = responseOrderList.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_OK, SC_OK, statusCode);

        // Получаем содержимое ответа в виде объекта orderList
        OrderList orderList = responseOrderList.extract().body().as(OrderList.class);
        OrderForList[] orders = orderList.getOrders(); // Получаем список заказов из ответа

        // Проверяем, что количество заказов в ответе > 0
        int actualNumberOfOrders = orders.length;
        Assert.assertTrue("Number of orsers shoul be > 0", actualNumberOfOrders > 0);
    }
}
