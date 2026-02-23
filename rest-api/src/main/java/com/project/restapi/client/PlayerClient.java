package com.project.restapi.client;

import com.project.restapi.models.*;
import com.project.restapi.utils.JsonMapper;
import io.qameta.allure.Step;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Service
public class PlayerClient extends AbstractApiClient {

    @Value("${player.api.base-url}")
    private String baseUrl;

    @Value("${player.api.get-user-by-id}")
    private String getUserByIdPath;

    @Value("${player.api.create-player}")
    private String createPlayerPath;

    @Value("${player.api.update-player}")
    private String updatePlayerPath;

    @Value("${player.api.delete-player}")
    private String deletePlayerPath;

    @Step("POST request: get player by Id")
    public <T> ApiResult<T> getPlayerById(PlayerRequest payload, int expectedStatus, Class<T> type) {
        return JsonMapper.map(post(baseUrl + getUserByIdPath, payload, expectedStatus), type);
    }

    @Step("GET request: create player")
    public <T> ApiResult<T> createPlayer(String editor, Map<String, Object> queryParams, int expectedStatus, Class<T> type) {
        return JsonMapper.map(get(String.format(baseUrl + createPlayerPath, editor), queryParams, expectedStatus), type);
    }

    @Step("PATCH request: update player")
    public <T> ApiResult<T> updatePlayer(String editor, String id, UpdatePlayerRequest body, int expectedStatus, Class<T> type) {
        return JsonMapper.map(patch(String.format(baseUrl + updatePlayerPath, editor, id), body, expectedStatus), type);
    }

    @Step("DELETE request: delete player by Id")
    public <T> ApiResult<T> deletePlayerById(String editor, PlayerRequest payload, int expectedStatus, Class<T> type) {
        return JsonMapper.map(delete(String.format(baseUrl + deletePlayerPath, editor), payload, expectedStatus), type);
    }
}
