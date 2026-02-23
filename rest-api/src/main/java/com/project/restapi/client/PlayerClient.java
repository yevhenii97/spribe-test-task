package com.project.restapi.client;

import com.project.restapi.models.*;
import com.project.restapi.utils.JsonMapper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
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

    @Step("Get player by Id request")
    public <T> ApiResult<T> getPlayerById(PlayerRequest payload, Class<T> type) {
        Response response = post(baseUrl + getUserByIdPath, payload);
        return JsonMapper.map(response, type);
    }

    @Step("Create player request")
    public <T> ApiResult<T> createPlayer(String editor, Map<String, Object> queryParams, Class<T> type) {
        Response response = get(String.format(baseUrl + createPlayerPath, editor), queryParams);
        return JsonMapper.map(response, type);
    }

    @Step("Update player request")
    public <T> ApiResult<T> updatePlayer(String editor, Long id, UpdatePlayerRequest body, Class<T> type) {
        Response response = patch(String.format(baseUrl + updatePlayerPath, editor, id), body);
        return JsonMapper.map(response, type);
    }

    @Step("Delete player by Id request")
    public <T> ApiResult<T> deletePlayerById(String editor, PlayerRequest payload, Class<T> type) {
        Response response = delete(String.format(baseUrl + deletePlayerPath, editor), payload);
        return JsonMapper.map(response, type);
    }
}
