package config;

public class Config {
    // URL приложения Яндекс.Самокат
    public static String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    // ручка для создания курьера
    public static String HANDLE_CREATING_COURIER = "/api/v1/courier";
    // ручка для создания ID курьера
    public static String HANDLE_LOGIN_COURIER = "/api/v1/courier/login";
    // ручка для удаления курьера
    public static String HANDLE_DELETE_COURIER= "/api/v1/courier/{courierId}";
    // ручка для работы с заказом: создание, получание списка заказов
    public static String HANDLE_ORDER_CREATE= "/api/v1/orders";
    // ручка для отмены заказа
    public static String HANDLE_ORDER_CANCELLATION = "/api/v1/orders/cancel";

}
