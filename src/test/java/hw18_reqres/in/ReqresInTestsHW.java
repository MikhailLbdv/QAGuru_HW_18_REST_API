package hw18_reqres.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.*;

public class ReqresInTestsHW {

    @Test
    @DisplayName("Проверка успешного создания пользователя")
    void testPostCreate() {
        String body = "{ \"name\": \"Testovui\", \"job\": \"QA\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Testovui"))
                .body("job", is("QA"))
                .body("id", is(notNullValue()))
                .body("createdAt", is(notNullValue()));
    }

    @Test
    @DisplayName("Проверка успешного удаления")
    void testDelete() {
        given()
                .log().uri()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }

    @Test
    @DisplayName("Проверка успешного редактирования пользователя")
    void testPutUpdate() {
        String body = "{ \"name\": \"TestovuiTest\", \"job\": \"QA Automation\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("TestovuiTest"))
                .body("job", is("QA Automation"))
                .body("updatedAt", is(notNullValue()));
    }

    @Test
    @DisplayName("Проверка, что пользователь не найден")
    void testSingleUserNotFound() {
        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body(is("{}"));
    }

    @Test
    @DisplayName("Проверка JsonScheme")
    void testSingleResource() {
        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .get("https://reqres.in/api/unknown/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/hw-jsonscheme-responce.json"));
    }

    @Test
    @DisplayName("Проверка StatusCode при отправке некорректного запроса без body")
    void unSuccessTestPostCreate2() {
        given()
                .log().uri()
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(415);
    }

    @Test
    @DisplayName("Проверка нудачной регистрации")
    void testRegisterUnsuccessful() {
        String body = "{ \"email\": \"incorrectemail@\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
