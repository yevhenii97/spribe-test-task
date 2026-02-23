package com.project.restapi.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.restapi.models.ApiResult;
import io.restassured.response.Response;

public class JsonMapper {

    private static final ObjectMapper OM =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonMapper() {}

    public static <T> ApiResult<T> map(Response response, Class<T> type) {
        String raw = extractBody(response);

        if (raw.isBlank()) {
            return new ApiResult<>(response.statusCode(), raw, null);
        }

        try {
            T dto = OM.readValue(raw, type);
            return new ApiResult<>(response.statusCode(), raw, dto);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed mapping to "
                            + type.getSimpleName()
                            + "\nBody:\n"
                            + raw,
                    e
            );
        }
    }

    public static ApiResult<Void> empty(Response response) {
        String raw = extractBody(response);
        return new ApiResult<>(response.statusCode(), raw, null);
    }

    private static String extractBody(Response response) {
        if (response == null || response.getBody() == null) {
            return "";
        }

        String raw = response.getBody().asString();
        return raw == null ? "" : raw;
    }
}
