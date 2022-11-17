package data;

// Класс для генерации цвета самоката
public class ColorGenerator {

    // Метод возвращает массив строк из двух цветов
    public static String[] blackAndGrey() {
        String[] color = {"BLACK", "GREY"};
        return color;
    }

    // Метод возвращает массив строк из цвета Black
    public static String[] black() {
        String[] color = {"BLACK"};
        return color;
    }

    // Метод возвращает массив строк из цвета Grey
    public static String[] grey() {
        String[] color = {"GREY"};
        return color;
    }

    // Метод возвращает массив строк без элементов
    public static String[] noElements() {
        String[] color = {};
        return color;
    }

    // Метод возвращает массив строк с пустым элементом
    public static String[] emptyElement() {
        String[] color = {""};
        return color;
    }
}
