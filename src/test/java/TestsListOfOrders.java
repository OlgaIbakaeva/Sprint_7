import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class TestsListOfOrders {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Тест Получение списка заказов")
    @Description("В теле ответа содержится Список заказов")
    public void testGettingListOfOrders() {
        ListOfOrder listOfOrder =
                given()
                        .header("Content-type", "application/json")
                        .get("/api/v1/orders")
                        .body()
                        .as(ListOfOrder.class);
        System.out.println("Список заказов:");
        String json = gson.toJson(listOfOrder);
        System.out.println(json);
    }
}
