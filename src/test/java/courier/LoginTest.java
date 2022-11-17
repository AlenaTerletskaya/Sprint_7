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

import java.util.List;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

// Класс с тестами по авторизации курьера
public class LoginTest {

    private CourierClient courierClient;
    private Courier courier;
    int id;
    private List<Courier> couriers;


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
    @DisplayName("Сourier can log in")
    @Description("Проверяет, что курьер может авторизоваться; успешный запрос возвращает код 200 и id курьера.")
    public void courierCanBeAuthorized() {

        courierClient.create(courier);
        ValidatableResponse response = courierClient.login(Credentials.from(courier));

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_OK, SC_OK, statusCode);

        id = response.extract().path("id");
        Assert.assertNotNull("Id value should not be null", id);
    }

    @Test
    @Tag("Negative")
    @DisplayName("Authorization under a non-existent courier returns an error")
    @Description("Проверяет, что если " +
            "авторизоваться под несуществующим пользователем / неправильно указан логин, " +
            "запрос возвращает ошибку")
    public void loginNonExistentCourier_returnError() {
        couriers = CourierGenerator.getCourierListErrorInLogin();

        courierClient.create(couriers.get(0));
        ValidatableResponse responseRightLogin = courierClient.login(Credentials.from(couriers.get(0)));
        id = responseRightLogin.extract().path("id");

        ValidatableResponse responseWrongLogin = courierClient.login(Credentials.from(couriers.get(1)));

        int actualStatusCode = responseWrongLogin.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_NOT_FOUND, SC_NOT_FOUND, actualStatusCode);

        String actualMessage = responseWrongLogin.extract().path("message");
        String expectedMessage = "Учетная запись не найдена";
        Assert.assertEquals("Message should be equal: " + expectedMessage, expectedMessage, actualMessage);
    }

    @Test
    @Tag("Negative")
    @DisplayName("Authorization with a non-existent password returns an error")
    @Description("Проверяет, что если неправильно указать пароль при авторизации, запрос возвращает ошибку")
    public void loginNonExistentPassword_returnError() {
        couriers = CourierGenerator.getCourierListErrorInPassword();

        courierClient.create(couriers.get(0));
        ValidatableResponse responseRightPassword = courierClient.login(Credentials.from(couriers.get(0)));
        id = responseRightPassword.extract().path("id");

        ValidatableResponse responseWrongPassword = courierClient.login(Credentials.from(couriers.get(1)));

        int actualStatusCode = responseWrongPassword.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_NOT_FOUND, SC_NOT_FOUND, actualStatusCode);

        String actualMessage = responseWrongPassword.extract().path("message");
        String expectedMessage = "Учетная запись не найдена";
        Assert.assertEquals("Message should be equal: " + expectedMessage, expectedMessage, actualMessage);
    }


}
