import SerializationPage.ListResource;
import SerializationPage.Register;
import SerializationPage.UsersData;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


public class MyTestAPI {
    // Задание: Используя сервис https://reqres.in/ получить список пользователей со второй страницы.
    // Убедится что имена файлов-аватаров пользователей совпадают
    @Test
    public void testNameAvatar() {
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpec());
        List<UsersData> data = given()
                .when()
                .get("api/users?page=2")
                .then()
                .statusCode(200)
                .log().body()
                .extract().body().jsonPath().getList("data", UsersData.class);

        Assert.assertTrue(data.stream().allMatch(x -> x.getAvatar().endsWith("128.jpg")));
    }


    // Протестировать регистрацию пользователя в системе.
    // Необходимо создание двух тестов: на успешную регистрацию
    @Test
    public void testRegister() {
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpec());
        Register register = new Register("eve.holt@reqres.in", "pistol");
        given()
                .contentType("application/json")
                .body(register)
                .when()
                .post("/api/register")
                .then()
                .log().body()
                .statusCode(200)
                .body("token",notNullValue())
                .body("id", notNullValue());
    }

    // и регистрацию с ошибкой из-за отсутствия пароля
    @Test
    public void testUnregister() {
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpec());
        given()
                .body(new Register("sydney@fife", ""))
                .contentType("application/json")
                .when()
                .post("/api/register")
                .then()
                .log().ifError()
                .body("password", notNullValue());
    }

    //Убедится что операция LIST <RESOURCE> возвращает данные отсортированные по годам
    @Test
    public void testList() {
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpec());
        List<ListResource> data = given()
                .when()
                .get("/api/unknown")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("data", ListResource.class);

        List<Integer> years = new ArrayList<>();
        for (ListResource listResource : data) {
            years.add(listResource.getYear());
        }

        List<Integer> checkYears = new ArrayList<>();
        for (Integer integer : years) {
            if (integer < (integer + 1)) {
                checkYears.add(integer);
            }
        }
        Assert.assertEquals(years, checkYears);
    }
}



