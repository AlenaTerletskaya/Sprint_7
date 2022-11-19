package data;

import clients.OrderClient;
import io.restassured.response.ValidatableResponse;
import models.order.Order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

// Класс для генерации заказа
public class OrderGenerator {

    // Метод возвращает заказ с данными
    public static Order getNewOrder() {
        String firstName = "Андрей";
        String lastName = "Иванов";
        String address = "123012, г. Москва, ул. Правды, д. 12, кв. 56";
        String metroStation = "Чистые пруды";
        String phone = "+79137894561";

        // Задаем период ареды - рандомное число от 1 до 7.
        Random random = new Random();
        int rentTime = random.nextInt(6) + 1;

        // Задаем дату доставки.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Готовим формат даты "дд.мм.гггг"
        // Получаем текущую дату, прибавляем к ней 1 день и форматируем
        String deliveryDate = LocalDate.now().plusDays(1).format(formatter);

        String comment = "Комментарий";
        String[] color = {"BLACK"};

        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    // Метод создает новый заказ и возвращает id заказа.
    public static int createOrderAndGetId(OrderClient orderClient) {
        Order order = OrderGenerator.getNewOrder(); // Генерируем новый заказ с данными
        ValidatableResponse responseOrder = orderClient.createOrder(order); // Создаем заказ
        int track = responseOrder.extract().path("track"); // Получаем трек-номер заказа

        // Получаем id заказа по его трек-номеру
        ValidatableResponse responseGetOrder = orderClient.getOrderByTrack(track);
        int orderId = responseGetOrder.extract().path("order.id");

        return orderId;
    }

    // Метод создает новый заказ и возвращает track и id заказа.
    public static int[] createOrderAndGetTrackAndId(OrderClient orderClient) {
        Order order = OrderGenerator.getNewOrder(); // Генерируем новый заказ с данными
        ValidatableResponse responseOrder = orderClient.createOrder(order); // Создаем заказ
        int track = responseOrder.extract().path("track"); // Получаем трек-номер заказа

        // Получаем id заказа по его трек-номеру
        ValidatableResponse responseGetOrder = orderClient.getOrderByTrack(track);
        int orderId = responseGetOrder.extract().path("order.id");

        return new int[] {track, orderId};
    }


}
