package com.project.restapi.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class AbstractApiClient {

    static {
        RestAssured.defaultParser = Parser.JSON;
    }

    protected Response get(String url, Map<String, Object> queryParams, int expectedStatus) {
        Response response = baseRequest()
                .queryParams(queryParams)
                .get(url);
        return execute(response, expectedStatus);
    }

    protected <V> Response post(String url, V body, int expectedStatus) {
        Response response = baseRequest()
                .body(body)
                .post(url);
        return execute(response, expectedStatus);
    }

    protected <V> Response patch(String url, V body, int expectedStatus) {
        Response response = baseRequest()
                .body(body)
                .patch(url);
        return execute(response, expectedStatus);
    }

    protected <V> Response delete(String url, V body, int expectedStatus) {
        Response response = baseRequest()
                .body(body)
                .delete(url);
        return execute(response, expectedStatus);
    }

    private Response execute(Response response, int expectedStatus) {
        ValidatableResponse validatable = response
                .then()
                .log().all()
                .statusCode(expectedStatus);
        return validatable.extract().response();
    }

    private RequestSpecification baseRequest() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all();
    }
}


