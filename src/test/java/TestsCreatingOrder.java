import com.google.gson.Gson;
import config.Config;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestsCreatingOrder {

    private Order order;
    private final OrderAPI orderAPI = new OrderAPI();
    private final Gson gson = new Gson();
    private final List<String> checkedColor;
    private final int expectedCode;

    public TestsCreatingOrder(List<String> checkedColor, int expectedCode) {
        this.checkedColor = checkedColor;
        this.expectedCode = expectedCode;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {List.of("BLACK"), 201},
                {List.of("GREY"), 201},
                {List.of("BLACK", "GREY"), 201},
                {List.of(), 201}
        };
    }

    @Before
    public void setUp() {
        // извлекаем из JSON-файла начальные тестовые данные и сериализуем в класс Order
        RestAssured.baseURI = Config.BASE_URL;
        try {
            Reader reader = new FileReader("src/test/resources/OrderData.json");
            order = gson.fromJson(reader, Order.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void CreatingOrderAndReturnTrack() {
        // присваиваем полю Цвет значение из массива данных для параметризации
        order.setColor(checkedColor);
        // создаем заказ
        Response response = orderAPI.create(order);
        // проваеряем, что заказ создался
        response.then().assertThat().body("track", notNullValue());
        // сравнениваем ОР и ФР по статус-коду ответа
        assertEquals(expectedCode, response.then().extract().statusCode());
        // после теста приводим БД заказов в исходное состояние
        orderAPI.clean(response);
    }
}

