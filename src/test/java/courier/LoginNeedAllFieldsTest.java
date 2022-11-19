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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

// Класс с параметризованным тестом по авторизации курьера со всеми обязательными полями
@RunWith (Parameterized.class)
public class LoginNeedAllFieldsTest {

    private CourierClient courierClient;
    private List<Courier> couriers;
    private int statusCode;
    private String message;
    private int id;
    private static final String MESSAGE = "Недостаточно данных для входа";

    public LoginNeedAllFieldsTest(List<Courier> couriers, int statusCode, String message) {
        this.couriers = couriers;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void CleanUp() {
        courierClient.delete(id);
    }

    // Тестовые данные
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object [][] {
                {CourierGenerator.getCourierListEmptyLogin(), SC_BAD_REQUEST, MESSAGE},
                {CourierGenerator.getCourierListNullLogin(), SC_BAD_REQUEST, MESSAGE},
                {CourierGenerator.getCourierListEmptyPassword(), SC_BAD_REQUEST, MESSAGE},
                // {CourierGenerator.getCourierListNullPassword(), SC_BAD_REQUEST, MESSAGE},
        };
    }

    @Test
    @Tag("Negative")
    @DisplayName("Authorization without required fields returns an error")
    @Description("Проверяет, что для авторизации нужно передать все обязательные поля. " +
            "Если одного из полей нет, запрос возвращает ошибку 400 и соответствующий текст.")
    public void courierLoginWithoutRequiredFields_returnError() {

        courierClient.create(couriers.get(0));
        ValidatableResponse responseRightLogin = courierClient.login(Credentials.from(couriers.get(0)));
        id = responseRightLogin.extract().path("id");

        ValidatableResponse responseWrongLogin = courierClient.login(Credentials.from(couriers.get(1)));

        int actualStatusCode = responseWrongLogin.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + statusCode, statusCode, actualStatusCode);

        String actualMessage = responseWrongLogin.extract().path("message");
        Assert.assertEquals("Message should be equal: " + message, message, actualMessage);
    }
}
