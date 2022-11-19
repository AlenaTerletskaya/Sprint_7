package models.order;

// Класс для POJO отмены заказа
public class OrderCancel {

    private final int track;

    public OrderCancel(int track) {
        this.track = track;
    }

    // Статический метод, который создает объект OrderCancel для отмены заказа по номеру трека.
    public static OrderCancel by(int track) {
        return new OrderCancel(track);
    }

    public int getTrack() {
        return track;
    }
}
