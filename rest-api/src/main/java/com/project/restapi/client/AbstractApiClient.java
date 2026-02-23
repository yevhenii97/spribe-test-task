package com.project.restapi.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class AbstractApiClient {

    static {
        RestAssured.defaultParser = Parser.JSON;
    }

    protected Response get(String url, Map<String, Object> queryParams) {
        return execute(baseRequest().queryParams(queryParams).get(url));
    }

    protected <V> Response post(String url, V body) {
        return execute(baseRequest().body(body).post(url));
    }

    protected <V> Response patch(String url, V body) {
        return execute(baseRequest().body(body).patch(url));
    }

    protected <V> Response delete(String url, V body) {
        return execute(baseRequest().body(body).delete(url));
    }

    private Response execute(Response response) {
        return response.then().log().all().extract().response();
    }

    private RequestSpecification baseRequest() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all();
    }
}


