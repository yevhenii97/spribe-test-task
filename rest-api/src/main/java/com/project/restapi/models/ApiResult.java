package com.project.restapi.models;

import java.util.Objects;

public class ApiResult<T> {

    private final int status;
    private final String rawBody;
    private final T body;

    public ApiResult(int status, String rawBody, T body) {
        this.status = status;
        this.rawBody = rawBody == null ? "" : rawBody;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getRawBody() {
        return rawBody;
    }

    public T getBody() {
        return body;
    }

    public boolean isEmptyBody() {
        return rawBody.isBlank();
    }

    public void assertEmptyBody() {
        if (!isEmptyBody()) {
            throw new AssertionError("Expected empty body but was:\n" + rawBody);
        }
    }

    @Override
    public String toString() {
        return "ApiResult{status=" + status + ", rawBody=" +
                (rawBody.length() > 300 ? rawBody.substring(0, 300) + "..." : rawBody) + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ApiResult<?> that)) return false;
        return status == that.status
                && Objects.equals(rawBody, that.rawBody)
                && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, rawBody, body);
    }
}
