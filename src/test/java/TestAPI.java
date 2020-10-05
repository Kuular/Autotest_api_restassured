import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestAPI {

    @org.testng.annotations.Test
    public  void firstTest1(){

    }

    @org.testng.annotations.Test
    public  void firstTest(){
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .statusCode(200)
                .body("page",notNullValue())
                .body("per_page",notNullValue())
                .body("data.first_name",hasItem("Lindsay"))
                .body("data.id",not(hasItem(nullValue())));
    }

    @org.testng.annotations.Test
    public  void secondTest(){
        Map<String,String> data = new HashMap<String, String>();
        data.put("name","kirill");
        data.put("job","teacher");
        Response response = given()
                .body(data)
                .contentType("application/json")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("name"),data.get("name"),"Имя не совпадает");
        Assert.assertEquals(jsonResponse.get("job"),data.get("job"),"Имя не совпадает");
    }

    @org.testng.annotations.Test
    public void test3(){
        ExtractableResponse<Response> allResponce =given()
                .contentType("application/json")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .extract();
    }

    @org.testng.annotations.Test
    public  void secondTestSpec(){
        Map<String,String> data = new HashMap<String, String>();
        data.put("name","kirill");
        data.put("job","teacher");
        Response response = given()
                .body(data)
                .spec(Specifications.requestSpec())
                .when()
                .post("/api/users")
                .then()
                .log().all()
                .spec(Specifications.responseSpec())
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("name"),data.get("name"),"Имя не совпадает");
        Assert.assertEquals(jsonResponse.get("job"),data.get("job"),"Имя не совпадает");
    }

    @org.testng.annotations.Test
    public  void secondTestSpecDefault(){
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());
        Map<String,String> data = new HashMap<String, String>();
        data.put("name","kirill");
        data.put("job","teacher");
        Response response = given()
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .log().all()
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("name"),data.get("name"),"Имя не совпадает");
        Assert.assertEquals(jsonResponse.get("job"),data.get("job"),"Имя не совпадает");
    }
}
