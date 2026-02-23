package com.project.tests;

import com.project.config.SpribeTestConfig;
import com.project.restapi.client.PlayerClient;
import com.project.restapi.models.CreatePlayerResponse;
import com.project.restapi.models.ErrorResponse;
import com.project.restapi.models.PlayerRequest;
import com.project.restapi.models.GetPlayerResponse;
import com.project.restapi.service.PlayerService;
import com.project.testdata.GetPlayerByIdDataProvider;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(classes = SpribeTestConfig.class)
@ContextConfiguration
@DirtiesContext
@Listeners({AllureTestNg.class})
@Epic("Player API")
@Feature("Get Player By ID")
public class GetPlayerByIdTests extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(GetPlayerByIdTests.class);

    @Autowired
    private PlayerClient playerClient;
    @Autowired
    private PlayerService playerService;

    Long randomId = ThreadLocalRandom.current().nextLong(1L, 1000L);

    @Test()
    @Severity(SeverityLevel.BLOCKER)
    @Description("Get player by id successfully")
    public void getPlayerByIdSuccessfully() {
        log.info("Thread ID: {}", Thread.currentThread().getId());

        CreatePlayerResponse createdPlayer = playerService.createBasePlayer();

        PlayerRequest playerRequest = new PlayerRequest(createdPlayer.getId());
        registerForCleanup(createdPlayer.getId());

        GetPlayerResponse response =
                playerClient.getPlayerById(playerRequest, 200, GetPlayerResponse.class)
                        .getBody();

        Assert.assertEquals(response.getAge(), createdPlayer.getAge(), "Expected age is NOT equal actual");
        Assert.assertEquals(response.getGender(), createdPlayer.getGender(), "Expected gender is NOT equal actual");
        Assert.assertEquals(response.getLogin(), createdPlayer.getLogin(), "Expected login is NOT equal actual");
        Assert.assertEquals(response.getRole(), createdPlayer.getRole(), "Expected role is NOT equal actual");
        Assert.assertEquals(response.getScreenName(), createdPlayer.getScreenName(), "Expected screenName is NOT equal actual");
    }

    @Test()
    @Severity(SeverityLevel.NORMAL)
    @Description("Get player by id unsuccessfully because of user does not exist")
    public void getPlayerByIdUnsuccessfullyBecauseOfUserDoesNotExist() {
        log.info("Thread ID: {}", Thread.currentThread().getId());

//      If I had access to DB, I would get max id + 1, and would not use random
        PlayerRequest playerRequest = new PlayerRequest(randomId);

        ErrorResponse response =
                playerClient.getPlayerById(playerRequest, 404, ErrorResponse.class)
                        .getBody();

        Assert.assertEquals(response.getStatus(), Integer.valueOf(404));
        Assert.assertEquals(response.getError(), "Not Found");
        Assert.assertEquals(response.getPath(), "/player/get");
    }

    @Test(dataProvider = "invalidPlayerIds", dataProviderClass = GetPlayerByIdDataProvider.class)
    @Severity(SeverityLevel.MINOR)
    @Description("Get player by id unsuccessfully because of playerId is incorrect")
    public void getPlayerByIdUnsuccessfullyBecauseOfPlayerIdIsIncorrect(Long playerId, String description) {
        log.info("Thread ID: {} | playerId: {}", Thread.currentThread().getId(), playerId);

        log.info("Testing invalid playerId case: {}", description);

        PlayerRequest playerRequest = new PlayerRequest(playerId);

        ErrorResponse response =
                playerClient.getPlayerById(playerRequest, 400, ErrorResponse.class)
                        .getBody();

        Assert.assertEquals(response.getStatus(), Integer.valueOf(400));
        Assert.assertEquals(response.getError(), "Bad Request");
        Assert.assertEquals(response.getPath(), "/player/get");
    }


}
