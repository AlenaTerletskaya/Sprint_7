package models.courier;

// Класс для POJO удаления курьера
public class CourierDeletion {

    private final int id;

    public CourierDeletion(int id) {
        this.id = id;
    }

    // Статический метод, который создает объект CourierDeletion для удаления курьера по id.
    public static CourierDeletion by(int id) {
        return new CourierDeletion(id);
    }

    public int getId() {
        return id;
    }

}
