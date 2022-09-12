import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.*;

@RunWith(Parameterized.class)
public class TestsCreatingOrder {

    private final File json;
    private final int expectedCode;

    private static final File json1 = new File("src/test/resources/OrderData1.json");
    private static final File json2 = new File("src/test/resources/OrderData2.json");
    private static final File json3 = new File("src/test/resources/OrderData3.json");
    private static final File json4 = new File("src/test/resources/OrderData4.json");

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";

    }

    public TestsCreatingOrder(File json, int expectedCode) {
        this.json = json;
        this.expectedCode = expectedCode;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                // в поле color один цвет BLACK
                {json1, 201},
                // в поле color один цвет GREY
                {json2, 201},
                // в поле color оба цвета
                {json3, 201},
                // поле color пусто
                {json4, 201}
        };
    }

    @Test
    @DisplayName("Параметризованный по полю color тест Создание заказа с получением номера заказа track")
    @Description("Создается номера заказа track и возвращается код 201. Далее приводим базу заказов в исходное состояние, удалив заказ по track")
    public void testCreatingOrderAndReturnTrack () {

        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue());
        // сравнение ОР и ФР по статус-коду ответа
        assertEquals(expectedCode, response.then().extract().statusCode());
        // получаем track заказа для последующего его удаления из базы
        OrderTrack orderTrack = response.body().as(OrderTrack.class);
        // отменяем заказ по track
        Response responseCancellation = given()
                .header("Content-type", "application/json")
                .body(orderTrack)
                .put("/api/v1/orders/cancel");
        int statusCancellation = responseCancellation.then().extract().statusCode();
        if (statusCancellation != 200) {
            System.out.println("БД заказов не приведена в исходное состояние");
            System.out.println("Баг: Созданный заказ " + orderTrack.getTrack() + " не отменяется");
        }
    }
}
