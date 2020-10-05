import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MyTestAPI {
    @Test
    public void testListPageSecond() {
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());
        given()
                .when()
                .get("api/users?page=2")
                .then()
                .log().all()
                .body("data", notNullValue());

    }

    //    взял имена файлов аватаров с первой страницы
    @Test
    public void testListAvatar() {
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());
        Map<String, String> avatar = new HashMap<String, String>();
        avatar.put("avatar", "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg");
        avatar.put("avatar", "https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg");
        avatar.put("avatar", "https://s3.amazonaws.com/uifaces/faces/twitter/olegpogodaev/128.jpg");
        avatar.put("avatar", "https://s3.amazonaws.com/uifaces/faces/twitter/marcoramires/128.jpg");
        avatar.put("avatar", "https://s3.amazonaws.com/uifaces/faces/twitter/stephenmoon/128.jpg");
        avatar.put("avatar", "https://s3.amazonaws.com/uifaces/faces/twitter/bigmancho/128.jpg");
        Response response = given()
                .body(avatar)
                .contentType("application/json")
                .when()
                .post("/api/users")
                .then()
                .log().all()
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("avatar"), avatar.get("avatar"), "Имя не совпадает");
    }


    @Test
    public void testRegister() {
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());
        Map<String, String> register = new HashMap<String, String>();
        register.put("email", "eve.holt@reqres.in");
        register.put("password", "pistol");

        Response response = given()
                .body(register)
                .contentType("application/json")
                .when()
                .post("/api/register")
                .then()
                .log().all()
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("email"), "4", "Почта не совпадает");
        Assert.assertEquals(jsonResponse.get("password"), "QpwL5tke4Pnpja7X4", "Пароль не совпадает");
    }


    @Test
    public void testUnregister() {
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());
        Map<String, String> register = new HashMap<String, String>();
        register.put("email", "sydney@fife");
        Response response = given()
                .body(register)
                .contentType("application/json")
                .when()
                .post("/api/register")
                .then()
                .log().all()
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("email"), register.get("email"), "Нет почты или пароля");
        Assert.assertEquals(jsonResponse.get("password"), register.get("password"), "Нет почты или пароля");
    }

    @Test
    public void testList(){
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());
        given()
                .when()
                .get("api/unknown")
                .then()
                .log().all()
                .body("data.year", hasItems(2000, 2001, 2002, 2003, 2004, 2005));
    }
}
