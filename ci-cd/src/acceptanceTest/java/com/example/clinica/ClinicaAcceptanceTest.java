package com.example.clinica;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ClinicaAcceptanceTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = System.getProperty("test.base.url", "http://localhost:9091");
    }

    @Test
    public void testGetAllClinics() {
        given()
                .auth().basic("admin", "admin")
                .when()
                .get("/clinics")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("_embedded.clinics", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testCreateClinic() {
        String clinicJson = """
                {
                    "name": "Test Clinic",
                    "address": "123 Test St",
                    "phone": "555-0123"
                }
                """;

        given()
                .auth().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(clinicJson)
                .when()
                .post("/clinics")
                .then()
                .statusCode(201)
                .body("name", equalTo("Test Clinic"));
    }

    @Test
    public void testHealthCheck() {
        given()
                .when()
                .get("/actuator/health")
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"));
    }
}