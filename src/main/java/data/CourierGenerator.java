package data;

import clients.CourierClient;
import io.restassured.response.ValidatableResponse;
import models.courier.Courier;
import models.courier.Credentials;

import java.util.List;

// Класс для генерации курьера
public class CourierGenerator {

    // Метод возвращает курьера с уникальными данными
    public static Courier getNewCourier() {
        long timestamp = System.currentTimeMillis();
        String login = "courier" + timestamp;
        String firstName = "name" + timestamp;
        String password = timestamp + "Apitest";
        return new Courier(login, password, firstName);
    }

    // Метод возвращает курьера c пустым логином
    public static Courier getCourierEmptyLogin() {
        Courier courier = CourierGenerator.getNewCourier();
        courier.setLogin("");
        return courier;
    }

    // Метод возвращает курьера c null логином
    public static Courier getCourierNullLogin() {
        Courier courier = CourierGenerator.getNewCourier();
        courier.setLogin(null);
        return courier;
    }

    // Метод возвращает курьера c пустым паролем
    public static Courier getCourierEmptyPassword() {
        Courier courier = CourierGenerator.getNewCourier();
        courier.setPassword("");
        return courier;
    }

    // Метод возвращает курьера c null логином
    public static Courier getCourierNullPassword() {
        Courier courier = CourierGenerator.getNewCourier();
        courier.setPassword(null);
        return courier;
    }

    // Метод возвращает список из 2х курьеров: 2й курьер отличается от 1го пустым логином.
    public static List<Courier> getCourierListEmptyLogin() {
        Courier courier = CourierGenerator.getNewCourier();
        Courier courierLogin = new Courier("", courier.getPassword(), courier.getFirstName());
        return List.of(courier, courierLogin);
    }

    // Метод возвращает список из 2х курьеров: 2й курьер отличается от 1го null логином.
    public static List<Courier> getCourierListNullLogin() {
        Courier courier = CourierGenerator.getNewCourier();
        Courier courierLogin = new Courier(null, courier.getPassword(), courier.getFirstName());
        return List.of(courier, courierLogin);
    }

    // Метод возвращает список из 2х курьеров: 2й курьер отличается от 1го пустым паролем.
    public static List<Courier> getCourierListEmptyPassword() {
        Courier courier = CourierGenerator.getNewCourier();
        Courier courierLogin = new Courier(courier.getLogin(), "", courier.getFirstName());
        return List.of(courier, courierLogin);
    }

    // Метод возвращает список из 2х курьеров: 2й курьер отличается от 1го null паролем.
    public static List<Courier> getCourierListNullPassword() {
        Courier courier = CourierGenerator.getNewCourier();
        Courier courierLogin = new Courier(courier.getLogin(), null, courier.getFirstName());
        return List.of(courier, courierLogin);
    }

    // Метод возвращает список из 2х курьеров: 2й курьер отличается от 1го логином - отсутствует последний символ.
    public static List<Courier> getCourierListErrorInLogin() {
        Courier courier = CourierGenerator.getNewCourier();
        String login = courier.getLogin();
        String newLogin = login.substring(0, login.length() - 1);
        Courier courierLogin = new Courier(newLogin, courier.getPassword(), courier.getFirstName());
        return List.of(courier, courierLogin);
    }

    // Метод возвращает список из 2х курьеров: 2й курьер отличается от 1го паролем - отсутствует последний символ.
    public static List<Courier> getCourierListErrorInPassword() {
        Courier courier = CourierGenerator.getNewCourier();
        String password = courier.getPassword();
        String newPassword = password.substring(0, password.length() - 1);
        Courier courierLogin = new Courier(newPassword, courier.getPassword(), courier.getFirstName());
        return List.of(courier, courierLogin);
    }

    // Метод создает нового курьера и возвращает его id.
    public static int createCourierAndGetId(CourierClient courierClient) {
        Courier courier = CourierGenerator.getNewCourier(); // Генерируем нового курьера с данными
        courierClient.create(courier); // Создаем курьера
        ValidatableResponse responseLogin = courierClient.login(Credentials.from(courier)); // Логинимся под курьером
        int courierId = responseLogin.extract().path("id"); // Получаем id курьера
        return courierId;
    }
}

