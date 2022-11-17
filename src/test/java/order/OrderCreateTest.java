package order;

import clients.OrderClient;
import data.ColorGenerator;
import data.OrderGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import models.order.Order;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;

// Класс с параметризованным тестом по созданию заказа
@RunWith(Parameterized.class)
public class OrderCreateTest {

    private OrderClient orderClient;
    private Order order;
    private String[] scooterColor;
    private int track;

    public OrderCreateTest(String[] scooterColor) {
        this.scooterColor = scooterColor;
    }

    @Before
    public void setUp() {

        orderClient = new OrderClient();
        order = OrderGenerator.getNewOrder(); // Генерируем данные нового заказа
    }

    @After
    public void CleanUp() {
        orderClient.cancelOrder(track);
    }

    // Тестовые данные
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object [][] {
                {ColorGenerator.black()},
                {ColorGenerator.grey()},
                {ColorGenerator.blackAndGrey()},
                {ColorGenerator.emptyElement()},
                {ColorGenerator.noElements()}
        };
    }

    @Test
    @Tag("Positive")
    @DisplayName("Order can be created with different colors")
    @Description("Проверяет, что когда создаёшь заказ, можно: " +
            "указать один из цветов — BLACK или GREY; указать оба цвета; совсем не указывать цвет. " +
            "Тело ответа содержит track.")
    public void orderCanBeCreatedDifferentColor() {

        order.setColor(scooterColor); // Меняем цвет в данных заказа на цвет из параметров
        ValidatableResponse response = orderClient.createOrder(order); // Создаем заказ

        // Проверяем статус-код заказа
        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_CREATED, SC_CREATED, statusCode);

        // Проверяем, что тело ответа содержит track
        track = response.extract().path("track");
        Assert.assertNotNull("Track should not be null", track);
    }
}
