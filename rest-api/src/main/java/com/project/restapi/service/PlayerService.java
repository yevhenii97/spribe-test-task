package com.project.restapi.service;

import com.project.restapi.client.PlayerClient;
import com.project.restapi.models.*;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class PlayerService {

    private static final Logger log = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private PlayerClient playerClient;

    @Step("Create base player")
    public CreatePlayerResponse createBasePlayer() {
        Map<String, Object> parameters = Map.of(
                "age", "20",
                "editor", "supervisor",
                "gender", "male",
                "login", "testLogin_" + UUID.randomUUID(),
                "role", "user",
                "screenName", "testScreenName_" + UUID.randomUUID(),
                "password", "pass" + (int)(Math.random() * 9000 + 1000)
        );

        ApiResult<CreatePlayerResponse> createdPlayer =
                playerClient.createPlayer("supervisor", parameters, CreatePlayerResponse.class);

        if (createdPlayer.getBody() == null) {
            throw new RuntimeException("Player was not created. Response: " + createdPlayer);
        }

        log.info("Player {} was created", createdPlayer);

        return createdPlayer.getBody();
    }

    @Step("Get player by id")
    public GetPlayerResponse getPlayer(Long id) {
        ApiResult<GetPlayerResponse> getPlayerResponse =
                playerClient.getPlayerById(new PlayerRequest(id), GetPlayerResponse.class);

        if (getPlayerResponse.getStatus() != 200) {
            throw new RuntimeException("Failed to get player. Status: " + getPlayerResponse.getStatus());
        }
        return getPlayerResponse.getBody();
    }

    @Step("Delete player")
    public void deleteBasePlayer(Long playerId) {

        playerClient.deletePlayerById(
                "supervisor",
                new PlayerRequest(playerId),
                Void.class
        );
        log.info("Player {} was deleted", playerId);
    }
}
