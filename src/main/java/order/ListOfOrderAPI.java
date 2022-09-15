package order;

import config.Config;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ListOfOrderAPI {

    public Response getListOfOrders() {
        return given()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URL)
                .get(Config.HANDLE_ORDER_CREATE);
    }

}
