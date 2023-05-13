package hw19;

import hw18_lombok.Specs;
import models_hw19.ReqresBodyLombokModel;
import models_hw19.ReqresResponseLombokModel;
import models_hw19.ErrorLombokModel;
import models.UserData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static hw18_lombok.Specs.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static specs.LoginSpec.requestSpec;
import static specs.LoginSpec.responseSpec;


@Tag("hw19_test")
@DisplayName("HW REST API. Модели. Спецификации.")
public class ReqresInTestsModelsAndSpecs {

    @Test
    @DisplayName("Проверка успешного создания пользователя")
    void testPostCreateLombokOptimized() {

        ReqresBodyLombokModel reqresBody = new ReqresBodyLombokModel();
        reqresBody.setName("Testovui");
        reqresBody.setJob("QA");

        String expectedName = "Testovui";
        String expectedJob = "QA";

        ReqresResponseLombokModel response = step("Отправка post запроса на создание пользователя", () ->
        given(requestSpec)
                .body(reqresBody)
                .when()
                .post("/users")
                .then()
                .spec(responseSpec)
                .statusCode(201)
                .extract().as(ReqresResponseLombokModel.class));

        step("Проверка введенного имени", () ->
                assertThat(response.getName()).isEqualTo(expectedName));
        step("Проверка введенной работы", () ->
                assertThat(response.getJob()).isEqualTo(expectedJob));
    }

    @Test
    @DisplayName("Проверка успешного удаления пользователя")
    void testDeleteLombokOptimized() {

        step("Отправка delete запроса на удаление пользователя", () ->
        given(requestSpec)
                .when()
                .delete("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(204));
    }

    @Test
    @DisplayName("Проверка успешного редактирования пользователя")
    void testPutUpdateLombokOptimized() {

        ReqresBodyLombokModel reqresBody = new ReqresBodyLombokModel();
        reqresBody.setName("TestovuiTest");
        reqresBody.setJob("QA Automation");

        String expectedName = "TestovuiTest";
        String expectedJob = "QA Automation";

        ReqresResponseLombokModel response = step("Отправка put запроса на изменение name и job пользователя ", () ->
        given(requestSpec)
                .body(reqresBody)
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(ReqresResponseLombokModel.class));

        step("Проверка введенного имени", () ->
                assertThat(response.getName()).isEqualTo(expectedName));
        step("Проверка введенной работы", () ->
                assertThat(response.getJob()).isEqualTo(expectedJob));
    }


    @Test
    @DisplayName("Проверка, что пользователь не найден")
    void testSingleUserNotFoundLombokOptimized() {

        step("Отправка get запроса по несуществующему пользователю", () ->
        given(requestSpec)
                .when()
                .get("/users/23")
                .then()
                .spec(responseSpec)
                .statusCode(404));
    }

    @Test
    @DisplayName("Проверка JsonScheme")
    void testSingleResourceLombokOptimized() {

        step("Отправка get запроса для проверки ответа", () ->
        given(requestSpec)
                .when()
                .get("/unknown/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/hw-jsonscheme-responce.json")));
    }

    @Test
    @DisplayName("Проверка нудачной регистрации")
    void testRegisterUnsuccessfulLombokOptimized() {

        ReqresBodyLombokModel reqresBody = new ReqresBodyLombokModel();
        reqresBody.setEmail("incorrectemail@");

        String expectedError = "Missing password";

        ErrorLombokModel response = step("Отправка post запроса c некорректым email", () ->
        given(requestSpec)
                .body(reqresBody)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec)
                .statusCode(400)
                .extract().as(ErrorLombokModel.class));

        step("Проверка получения ошибки", () ->
                assertThat(response.getError()).isEqualTo(expectedError));
    }

    @Disabled("Я пытался, но не смог)")
    @Test
    @DisplayName("Проверка получения данных по определенному пользователю")
    void testSingleUserLombokOptimized() {


        UserData data = Specs.request  //  Пытался Specs заменить на LoginSpec, а request на given(requestSpec), но что-то пошло не так)
                // + и через step не получилось

                .spec(request)
                .when()
                .get("/users/7")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(models.UserData.class);

        step("Проверка id пользователя", () ->
                assertThat(data.getUser().getId()).isEqualTo(7));
        step("Проверка email пользователя", () ->
                assertThat(data.getUser().getEmail()).isEqualTo("michael.lawson@reqres.in"));
        step("Проверка firstName пользователя", () ->
                assertThat(data.getUser().getFirstName()).isEqualTo("Michael"));
        step("Проверка lastName пользователя", () ->
                assertThat(data.getUser().getLastName()).isEqualTo("Lawson"));
    }
}
