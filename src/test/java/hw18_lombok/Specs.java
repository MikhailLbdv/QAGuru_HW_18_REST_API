package hw18_lombok;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;

public class Specs {

    public static RequestSpecification request = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .log().all()
            .contentType(ContentType.JSON);

    public static ResponseSpecification responseSpec200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();

    public static ResponseSpecification responseSpec201 = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .build();

    public static ResponseSpecification responseSpec204 = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .build();

    public static ResponseSpecification responseSpec400 = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .build();

    public static ResponseSpecification responseSpec404 = new ResponseSpecBuilder()
            .expectStatusCode(404)
            .build();
}
