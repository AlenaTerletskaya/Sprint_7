package courier;

import clients.CourierClient;
import data.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import models.courier.Courier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

// Класс с параметризованным тестом по созданию курьера со всеми обязательными полями
@RunWith(Parameterized.class)
public class CourierCreateNeedAllFieldsTest {
    private CourierClient courierClient;
    private Courier courier;
    private int statusCode;
    private String message;
    private static final String MESSAGE = "Недостаточно данных для создания учетной записи";

    public CourierCreateNeedAllFieldsTest(Courier courier, int statusCode, String message) {
        this.courier = courier;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    // Тестовые данные
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object [][] {
                {CourierGenerator.getCourierEmptyLogin(), SC_BAD_REQUEST, MESSAGE},
                {CourierGenerator.getCourierNullLogin(), SC_BAD_REQUEST, MESSAGE},
                {CourierGenerator.getCourierEmptyPassword(), SC_BAD_REQUEST, MESSAGE},
                {CourierGenerator.getCourierNullPassword(), SC_BAD_REQUEST, MESSAGE}
        };
    }

    @Test
    @Tag("Negative")
    @DisplayName("Creating a courier without required fields returns an error")
    @Description("Проверяет, что для создания курьера нужно передать все обязательные поля. " +
            "Если одного из полей нет, запрос возвращает ошибку 400 и соответствующий текст.")
    public void courierCreateWithoutRequiredFields_returnError() {

        ValidatableResponse responseCreate = courierClient.create(courier);

        int actualStatusCode = responseCreate.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + statusCode, statusCode, actualStatusCode);

        String actualMessage = responseCreate.extract().path("message");
        Assert.assertEquals("Message should be equal: " + message, message, actualMessage);
    }
}
