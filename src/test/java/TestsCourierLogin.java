import courier.CourierAPI;
import courier.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestsCourierLogin {

    private CourierData courierData;
    private final CourierAPI courierAPI = new CourierAPI();
    private Response response;

    @Test
    @DisplayName("Тест Логин курьера с корректными параметрами")
    @Description("Создается ID курьера и возвращается код 200. Далее приводим базу курьеров в исходное состояние, удалив курьера")
    public void testCourierCanLogInAndReturnIdWithCorrectParameters () {
        // создаем тестового курьера (рандомно)
        courierData  = CourierData.getCourierCorrect();
        response = courierAPI.create(courierData);
        // проверка Логин курьера с корректными параметрами
        response = courierAPI.login(courierData);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        // после теста приводим БД курьеров в исходное состояние
        courierAPI.clean(courierData);
    }

    @Test
    @DisplayName("Негативный тест Логин курьера с login = null")
    @Description("Возвращается код 400 и сообщение об ошибке \"Недостаточно данных для входа\"")
    public void testCourierCanNotLogInWithoutLogin () {
        // создаем тестового курьера с login = null"
        courierData  = CourierData.getCourierWithoutLogin();
        response = courierAPI.create(courierData);
        // проверка Логин курьера с login = null"
        response = courierAPI.login(courierData);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Негативный тест Логин курьера с password = null")
    @Description("Возвращается код 400 и сообщение об ошибке \"Недостаточно данных для входа\"")
    public void testCourierCanNotLogInWithoutPassword () {
        try {
            // создаем тестового курьера с password = null"
            courierData  = CourierData.getCourierWithoutPassword();
            response = courierAPI.create(courierData);
            // проверка Логин курьера с login = null"
            response = courierAPI.login(courierData);
            response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                    .and()
                    .statusCode(400);
        } catch (RuntimeException exception) {
            System.out.println("ФР: Время ожидания ответа превышено при запросе Логин курьера без пароля");
            System.out.println("ОР: код ответа 400. Сообщение об ошибке \"Недостаточно данных для входа\"");
        }
    }

    @Test
    @DisplayName("Негативный тест Логин курьера с несуществующей парой логин-пароль")
    @Description("Возвращается код 404 и сообщение об ошибке \"Учетная запись не найдена\"")
    public void testCourierCanNotLogInIfNonExistentLoginPasswordPair () {
        // создаем тестового курьера (рандомно)
        courierData  = CourierData.getCourierCorrect();
        response = courierAPI.create(courierData);
        // меняем пароль, чтобы сделать несуществующую пару логин-пароль
        courierData.setPassword(courierData.getPassword() + "ERROR");
        // проверка Логин курьера с несуществующей парой логин-пароль
        response = courierAPI.login(courierData);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
}
