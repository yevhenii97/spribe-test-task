package com.project.restapi.service;

import com.project.restapi.client.PlayerClient;
import com.project.restapi.models.*;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import java.util.Map;

@Service
public class PlayerService {

    @Autowired
    private PlayerClient playerClient;

    @Step("Create base player")
    public CreatePlayerResponse createBasePlayer() {

        Map<String, Object> parameters = Map.of(
                "age", "20",
                "editor", "supervisor",
                "gender", "male",
                "login", "testLogin_" + System.currentTimeMillis(),
                "role", "user",
                "screenName", "testScreenName_" + System.currentTimeMillis(),
                "password", "testPassword"
        );

        return playerClient.createPlayer("supervisor", parameters, 200, CreatePlayerResponse.class).getBody();
    }

    @Step("Delete player")
    public void deleteBasePlayer(Long playerId) {

        playerClient.deletePlayerById(
                "supervisor",
                new PlayerRequest(playerId),
                204,
                Void.class
        );
    }

    @Step("Assert updated player")
    public void assertUpdatedPlayer(
            GetPlayerResponse before,
            UpdatePlayerResponse updated,
            UpdatePlayerRequest req,
            String description
    ) {

        Assert.assertEquals(updated.getId(), before.getId(), "[id must not change] " + description);

        if (req.getAge() != null) {
            Assert.assertEquals(updated.getAge(), req.getAge(), "[age updated] " + description);

        } else {
            Assert.assertEquals(updated.getAge(), before.getAge(), "[age unchanged] " + description);
        }

        if (req.getGender() != null) {
            Assert.assertEquals(updated.getGender(), req.getGender(), "[gender updated] " + description);

        } else {
            Assert.assertEquals(updated.getGender(), before.getGender(), "[gender unchanged] " + description);
        }

        if (req.getLogin() != null) {
            Assert.assertEquals(updated.getLogin(), req.getLogin(), "[login updated] " + description);

        } else {
            Assert.assertEquals(updated.getLogin(), before.getLogin(), "[login unchanged] " + description);
        }

        if (req.getScreenName() != null) {
            Assert.assertEquals(updated.getScreenName(), req.getScreenName(), "[screenName updated] " + description);

        } else {
            Assert.assertEquals(updated.getScreenName(), before.getScreenName(), "[screenName unchanged] " + description);
        }

        if (req.getRole() != null) {
            Assert.assertEquals(updated.getRole(), req.getRole(), "[role updated] " + description);

        } else {
            Assert.assertEquals(updated.getRole(), before.getRole(), "[role unchanged] " + description);
        }
    }
}
