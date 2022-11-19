package models.order;

public class OrderFinish {

    private final int id;

    public OrderFinish(int id) {
        this.id = id;
    }

    // Статический метод, который создает объект OrderFinish для отмены заказа по номеру id.
    public static OrderFinish by(int id) {
        return new OrderFinish(id);
    }

    public int getId() {
        return id;
    }
}
