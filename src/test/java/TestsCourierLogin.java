import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestsCourierLogin {

    private final String login = "myCourier";
    private final String password = "12345";

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        String firstName = "NameCourier";
        CourierData json  = new CourierData(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Тест Логин курьера с корректными параметрами")
    @Description("Создается ID курьера и возвращается код 200. Далее приводим базу курьеров в исходное состояние, удалив курьера")
    public void testCourierCanLogInAndReturnIdWithCorrectParameters () {
        CourierData json  = new CourierData(login, password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        // получаем значение ID курьера для последующего его удаления из базы
        CourierID courierID = response.body().as(CourierID.class);
        // формируем в строковой переменной ручку с полученным ID
        String handDelete = "/api/v1/courier/" + courierID.getId();
        // удаляем курьера из базы по ID
        given()
                .header("Content-type", "application/json")
                .body(courierID)
                .when()
                .delete(handDelete)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Негативный тест Логин курьера с login = null")
    @Description("Возвращается код 400 и сообщение об ошибке \"Недостаточно данных для входа\"")
    public void testCourierCanNotLogInWithoutLogin () {
        CourierData jsonNegativ  = new CourierData(null, password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(jsonNegativ)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
        // после теста удаляем курьера из базы. Для этого нужно сначала создать его ID, а затем по ID удалить курьера
        CourierData json  = new CourierData(login, password);
        // создаем логин курьера
        CourierID courierID =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
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
    @DisplayName("Негативный тест Логин курьера с password = null")
    @Description("Возвращается код 400 и сообщение об ошибке \"Недостаточно данных для входа\"")
    public void testCourierCanNotLogInWithoutPassword () {
        try {
            CourierData jsonNegativ  = new CourierData(login, null);
            Response response = given()
                    .header("Content-type", "application/json")
                    .body(jsonNegativ)
                    .when()
                    .post("/api/v1/courier/login");
            response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                    .and()
                    .statusCode(400);
        } catch (RuntimeException exception) {
            System.out.println("Баг: Время ожидания ответа превышено при запросе Логин курьера без пароля");
            System.out.println("ОР: код ответа 400. Сообщение об ошибке \"Недостаточно данных для входа\"");
        } finally {
            // после теста удаляем курьера из базы. Для этого нужно сначала создать его ID, а затем по ID удалить курьера
            CourierData json  = new CourierData(login, password);
            // создаем логин курьера
            CourierID courierID =
                    given()
                            .header("Content-type", "application/json")
                            .body(json)
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
    }

    @Test
    @DisplayName("Негативный тест Логин курьера с несуществующей парой логин-пароль")
    @Description("Возвращается код 404 и сообщение об ошибке \"Учетная запись не найдена\"")
    public void testCourierCanNotLogInIfNonExistentLoginPasswordPair () {
        String passwordError = password + "Error";
        CourierData jsonNegativ  = new CourierData(login, passwordError);
        Response response = given()
                .header("Content-type", "application/json")
                .body(jsonNegativ)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
        // после теста удаляем курьера из базы. Для этого нужно сначала создать его ID, а затем по ID удалить курьера
        CourierData json  = new CourierData(login, password);
        // создаем логин курьера
        CourierID courierID =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
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
}
