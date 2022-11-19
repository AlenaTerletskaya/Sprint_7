package courier;

import clients.CourierClient;
import data.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import models.courier.Courier;
import models.courier.Credentials;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

// Класс с тестами по удалению курьера
public class CourierDeleteTest {
    private CourierClient courierClient;
    private Courier courier;
    int id;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @Tag("Positive")
    @DisplayName("Сourier can be deleted")
    @Description("Проверяет, что созданного курьера можно удалить. " +
            "Успешный запрос возвращает код 200 и соответствующий текст.")
    public void courierCanBeDeleted() {
        courier = CourierGenerator.getNewCourier();
        courierClient.create(courier);
        ValidatableResponse responseLogin = courierClient.login(Credentials.from(courier));
        id = responseLogin.extract().path("id");

        ValidatableResponse responseDelete = courierClient.delete(id);

        int statusCode = responseDelete.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_OK, SC_OK, statusCode);

        boolean isCourierDeleted = responseDelete.extract().path("ok");
        Assert.assertTrue("Value should be equal: true", isCourierDeleted);
    }

    @Test
    @Ignore
    @Tag("Negative")
    @DisplayName("Request to delete a courier with null id returns an error")
    @Description("Проверяет, что если отправить запрос на удаление курьера c null id, " +
            "вернётся ошибка 400 и соответствующий текст.")
    public void courierDeleteNullId_returnError() {

        ValidatableResponse responseDelete = courierClient.deleteNullId();

        int statusCode = responseDelete.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_BAD_REQUEST, SC_BAD_REQUEST, statusCode);

        String actualMessage = responseDelete.extract().path("message");
        String message = "Недостаточно данных для удаления курьера";
        Assert.assertEquals("Message should be equal: " + message, message, actualMessage);
    }

    @Test
    @Tag("Negative")
    @DisplayName("Request to delete a courier with non-existent id returns an error")
    @Description("Проверяет, что если отправить запрос на удаление курьера c несуществующим id, " +
            "вернётся ошибка 404 и соответствующий текст.")
    public void courierDeleteNonExistentId_returnError() {

        ValidatableResponse responseDelete = courierClient.delete(0);

        int statusCode = responseDelete.extract().statusCode();
        Assert.assertEquals("Status code should be equal: " + SC_NOT_FOUND, SC_NOT_FOUND, statusCode);

        String actualMessage = responseDelete.extract().path("message");
        String message = "Курьера с таким id нет.";
        Assert.assertEquals("Message should be equal: " + message, message, actualMessage);
    }
}
