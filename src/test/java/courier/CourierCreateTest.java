package courier;

import clients.CourierClient;
import data.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import models.courier.Courier;
import models.courier.Credentials;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;

// Класс с тестами по созданию курьера
public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;
    int id;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getNewCourier();
    }

    @After
    public void cleanUp() {
        courierClient.delete(id);
    }

    @Test
    @Tag("Positive")
    @DisplayName("Сourier can be created")
    @Description("Проверяет, что курьера можно создать. Успешный запрос возвращает код 201 и соответствующий текст.")
    public void courierCanBeCreated() {

        ValidatableResponse responseCreate = courierClient.create(courier);
        ValidatableResponse responseLogin = courierClient.login(Credentials.from(courier));
        id = responseLogin.extract().path("id");

        int statusCode = responseCreate.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_CREATED, SC_CREATED, statusCode);

        boolean isCourierCreated = responseCreate.extract().path("ok");
        Assert.assertTrue("Value should be equal: true", isCourierCreated);
    }

    @Test
    @Tag("Negative")
    @DisplayName("Creating two identical couriers returns an error")
    @Description("Проверяет, что нельзя создать двух одинаковых курьеров. " +
            "Eсли создать курьера с логином, который уже есть, возвращается ошибка.")
    public void twoEqualCouriersCanNotBeCreated() {

        // Создаем первого курьера
        courierClient.create(courier);
        // Создаем второго курьера, идентичного первому
        ValidatableResponse responseSecondCreate = courierClient.create(courier);

        // Логинимся под курьером, чтобы получить id
        ValidatableResponse responseLogin = courierClient.login(Credentials.from(courier));
        id = responseLogin.extract().path("id");

        int secondCreateStatusCode = responseSecondCreate.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_CONFLICT,
                SC_CONFLICT, secondCreateStatusCode);

        String secondCreateMessage = responseSecondCreate.extract().path("message");
        String expectedMessage = "Этот логин уже используется. Попробуйте другой.";
        Assert.assertEquals("Value should be equal: " + expectedMessage, expectedMessage, secondCreateMessage);
    }
}

