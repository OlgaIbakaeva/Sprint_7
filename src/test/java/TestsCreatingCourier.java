import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class TestsCreatingCourier {

    private final String login = "myCourier";
    private final String password = "12345";
    private final String firstName = "NameCourier";

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Тест Создание курьера с корректными параметрами")
    @Description("Логин и пароль курьера помещаются в таблицу Couriers в БД. Возвращается код 200. Далее приводим базу курьеров в исходное состояние, удалив курьера")
    public void testCanCreateCourierWithCorrectParameters() {
        CourierData json  = new CourierData(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
        // после теста удаляем курьера из БД. Для этого нужно сначала создать его ID, а затем по ID удалить курьера
        CourierData jsonCourier  = new CourierData(login, password);
        // создаем логин курьера
        CourierID courierID =
                given()
                        .header("Content-type", "application/json")
                        .body(jsonCourier)
                        .post("/api/v1/courier/login")
                        .body()
                        .as(CourierID.class);
        // формируем в строковой переменной ручку с полученным ID
        String handDelete = "/api/v1/courier/" + courierID.getId();
        // удаляем курьера из базы по ID
        given()
                .header("Content-type", "application/json")
                .body(courierID)
                .delete(handDelete)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Тест Нельзя создать двух одинаковых курьеров")
    @Description("При повторном создании курьера с теми же параметрами возвращается код 409 и сообщение \"Этот логин уже используется. Попробуйте другой.\". Далее приводим базу курьеров в исходное состояние, удалив курьера")
    public void testCanNotCreateTwoIdenticalCouriers() {
        CourierData json  = new CourierData(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        // после теста удаляем курьера из БД. Для этого нужно сначала создать его ID, а затем по ID удалить курьера
        CourierData jsonCourier  = new CourierData(login, password);
        // создаем логин курьера
        CourierID courierID =
                given()
                        .header("Content-type", "application/json")
                        .body(jsonCourier)
                        .post("/api/v1/courier/login")
                        .body()
                        .as(CourierID.class);
        // формируем в строковой переменной ручку с полученным ID
        String handDelete = "/api/v1/courier/" + courierID.getId();
        // удаляем курьера из базы по ID
        given()
                .header("Content-type", "application/json")
                .body(courierID)
                .delete(handDelete)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Негативный тест Создание курьера без логина")
    @Description("Возвращается код 400 и сообщение об ошибке \"Недостаточно данных для создания учетной записи\"")
    public void testCanNotCreateCourierWithoutLogin() {
        CourierData json  = new CourierData(null, password, firstName);
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Негативный тест Создание курьера без пароля")
    @Description("Возвращается код 400 и сообщение об ошибке \"Недостаточно данных для создания учетной записи\"")public void testCanNotCreateCourierWithoutPassword() {
        CourierData json  = new CourierData(login, null, firstName);
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
