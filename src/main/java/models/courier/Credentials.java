package models.courier;

// Класс для POJO данных для логина курьера
public class Credentials {

    private String login;
    private String password;

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Статический метод, который создает экземпляр Credentials на основе курьера.
    public static Credentials from(Courier courier) {
        return new Credentials(courier.getLogin(), courier.getPassword());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

