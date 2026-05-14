package com.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PetstoreTeste {

    @Test
    public void testGetRequest() {
        // Example API test using REST Assured with Petstore API
        given()
            .when()
                .get("https://petstore.swagger.io/v2/pet/findByStatus?status=available")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetPetById() {
        // Test to get pet by ID 12
        Response response = given()
            .when()
                .get("https://petstore.swagger.io/v2/pet/12")
            .then()
                .statusCode(200)
                .body("id", equalTo(12))
                .extract().response();

        // Log the response
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @Test
    public void testGetPetByIdNotFound() {
        // Test to get pet by a non-existent ID 99999999, expecting 404
        given()
            .when()
                .get("https://petstore.swagger.io/v2/pet/99999999")
            .then()
                .statusCode(404);
    }
}
