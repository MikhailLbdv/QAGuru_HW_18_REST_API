package hw18_lombok;

import models.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hw18_lombok.Specs.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresInTestsHWLombok {

    @Test
    @DisplayName("Проверка успешного создания пользователя")
    void testPostCreateLombok() {
        String body = "{ \"name\": \"Testovui\", \"job\": \"QA\" }";

        given()
                .spec(request)
                .body(body)
                .when()
                .post("/users")
                .then()
                .log().all()
                .spec(responseSpec201)
                .body("name", is("Testovui"))
                .body("job", is("QA"))
                .body("id", is(notNullValue()))
                .body("createdAt", is(notNullValue()));
    }

    @Test
    @DisplayName("Проверка успешного удаления")
    void testDeleteLombok() {
        given()
                .spec(request)
                .when()
                .delete("/users/2")
                .then()
                .log().all()
                .spec(responseSpec204);
    }

    @Test
    @DisplayName("Проверка успешного редактирования пользователя")
    void testPutUpdateLombok() {
        String body = "{ \"name\": \"TestovuiTest\", \"job\": \"QA Automation\" }";

        given()
                .spec(request)
                .body(body)
                .when()
                .put("/users/2")
                .then()
                .log().all()
                .spec(responseSpec200)
                .body("name", is("TestovuiTest"))
                .body("job", is("QA Automation"))
                .body("updatedAt", is(notNullValue()));
    }


    @Test
    @DisplayName("Проверка, что пользователь не найден")
    void testSingleUserNotFoundLombok() {
        given()
                .spec(request)
                .when()
                .get("/users/23")
                .then()
                .log().all()
                .spec(responseSpec404)
                .body(is("{}"));
    }

    @Test
    @DisplayName("Проверка JsonScheme")  //https://www.liquid-technologies.com/online-json-to-schema-converter
    void testSingleResourceLombok() {
        given()
                .spec(request)
                .when()
                .get("/unknown/2")
                .then()
                .log().all()
                .spec(responseSpec200)
                .body(matchesJsonSchemaInClasspath("schemes/hw-jsonscheme-responce.json"));
    }

    @Test
    @DisplayName("Проверка нудачной регистрации")
    void testRegisterUnsuccessfulLombok() {
        String body = "{ \"email\": \"incorrectemail@\" }";

        given()
                .spec(request)
                .body(body)
                .when()
                .post("/register")
                .then()
                .log().all()
                .spec(responseSpec400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Проверка получения корректных данных по определенному пользователю")
    void testSingleUserLombok() {
        UserData data = Specs.request
                .spec(request)
                .when()
                .get("/users/7")
                .then()
                .log().all()
                .spec(responseSpec200)
                .extract().as(models.UserData.class);

        assertEquals(7, data.getUser().getId());
        assertEquals("michael.lawson@reqres.in", data.getUser().getEmail());
        assertEquals("Michael", data.getUser().getFirstName());
        assertEquals("Lawson", data.getUser().getLastName());
    }
}
