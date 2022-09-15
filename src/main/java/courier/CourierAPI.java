package courier;
import config.Config;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierAPI {

    public Response create(CourierData courierData) {

        return given()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(courierData)
                .post(Config.HANDLE_CREATING_COURIER);
    }

    public Response login(CourierData courierData) {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(courierData)
                .post(Config.HANDLE_LOGIN_COURIER);
    }

    public void delete(String courierId) {
        given()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .pathParam("courierId", courierId)
                .when()
                .delete(Config.HANDLE_DELETE_COURIER)
                .then()
                .assertThat()
                .statusCode(200);
    }

    // удаляем тестового курьера из БД. Для этого нужно сначала создать его ID, а затем по ID удалить курьера
    public void clean(CourierData courierData) {
        Response response = login(courierData);
        CourierID courierID = response.body().as(CourierID.class);
        delete(courierID.getId());
    }
}
