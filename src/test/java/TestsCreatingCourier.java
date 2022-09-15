import courier.CourierAPI;
import courier.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestsCreatingCourier {

    private CourierData courierData;
    private final CourierAPI courierAPI = new CourierAPI();
    private Response response;

    @Test
    @DisplayName("Тест Создание курьера с корректными параметрами")
    @Description("Логин и пароль курьера помещаются в таблицу Couriers в БД. Возвращается код 200. Далее приводим базу курьеров в исходное состояние, удалив курьера")
    public void testCanCreateCourierWithCorrectParameters() {
        courierData  = CourierData.getCourierCorrect();
        response = courierAPI.create(courierData);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
        // после теста приводим БД курьеров в исходное состояние
        courierAPI.clean(courierData);
    }

    @Test
    @DisplayName("Тест Нельзя создать двух одинаковых курьеров")
    @Description("При повторном создании курьера с теми же параметрами возвращается код 409 и сообщение \"Этот логин уже используется. Попробуйте другой.\". Далее приводим базу курьеров в исходное состояние, удалив курьера")
    public void testCanNotCreateTwoIdenticalCouriers() {
        courierData  = CourierData.getCourierCorrect();
        response = courierAPI.create(courierData);
        response.then().statusCode(201);
        response = courierAPI.create(courierData);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
        // после теста приводим БД курьеров в исходное состояние
        courierAPI.clean(courierData);
    }

    @Test
    @DisplayName("Негативный тест Создание курьера без логина")
    @Description("Возвращается код 400 и сообщение об ошибке \"Недостаточно данных для создания учетной записи\"")
    public void testCanNotCreateCourierWithoutLogin() {
        courierData  = CourierData.getCourierWithoutLogin();
        response = courierAPI.create(courierData);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Негативный тест Создание курьера без пароля")
    @Description("Возвращается код 400 и сообщение об ошибке \"Недостаточно данных для создания учетной записи\"")
    public void testCanNotCreateCourierWithoutPassword() {
        courierData  = CourierData.getCourierWithoutPassword();
        response = courierAPI.create(courierData);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}
