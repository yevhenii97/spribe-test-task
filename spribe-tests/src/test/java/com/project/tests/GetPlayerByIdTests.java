package com.project.tests;

import com.project.config.SpribeTestConfig;
import com.project.restapi.client.PlayerClient;
import com.project.restapi.models.*;
import com.project.testdata.GetPlayerByIdDataProvider;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(classes = SpribeTestConfig.class)
@Listeners({AllureTestNg.class})
@Epic("Player API")
@Feature("Get Player By ID")
public class GetPlayerByIdTests extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(GetPlayerByIdTests.class);

    @Autowired
    private PlayerClient playerClient;

    @Test()
    @Severity(SeverityLevel.BLOCKER)
    @Description("Get player by id successfully")
    public void getPlayerByIdSuccessfully() {
        CreatePlayerResponse createdPlayer = playerService.createBasePlayer();

        PlayerRequest playerRequest = new PlayerRequest(createdPlayer.getId());
        registerForCleanup(createdPlayer.getId());

        ApiResult<GetPlayerResponse> response =
                playerClient.getPlayerById(playerRequest, GetPlayerResponse.class);

        Assert.assertEquals(response.getStatus(), 200, "Expected status code is not equal actual");
        Assert.assertEquals(response.getBody().getAge(), createdPlayer.getAge(), "Expected age is NOT equal actual");
        Assert.assertEquals(response.getBody().getGender(), createdPlayer.getGender(), "Expected gender is NOT equal actual");
        Assert.assertEquals(response.getBody().getLogin(), createdPlayer.getLogin(), "Expected login is NOT equal actual");
        Assert.assertEquals(response.getBody().getRole(), createdPlayer.getRole(), "Expected role is NOT equal actual");
        Assert.assertEquals(response.getBody().getScreenName(), createdPlayer.getScreenName(), "Expected screenName is NOT equal actual");
    }

    @Test()
    @Severity(SeverityLevel.NORMAL)
    @Description("Get player by id unsuccessfully because of user does not exist")
    public void getPlayerByIdUnsuccessfullyBecauseOfUserDoesNotExist() {
//      If I had access to DB, I would get max id + 1, and would not use random
        PlayerRequest playerRequest = new PlayerRequest(
                ThreadLocalRandom.current().nextLong(Long.MAX_VALUE / 2, Long.MAX_VALUE)
        );

        ApiResult<ErrorResponse> response =
                playerClient.getPlayerById(playerRequest, ErrorResponse.class);

        Assert.assertEquals(response.getStatus(), 404, "Expected status code is not equal actual");
        Assert.assertEquals(response.getBody().getError(), "Not Found");
        Assert.assertEquals(response.getBody().getPath(), "/player/get");
    }

    @Test(dataProvider = "invalidPlayerIds", dataProviderClass = GetPlayerByIdDataProvider.class)
    @Severity(SeverityLevel.MINOR)
    @Description("Get player by id unsuccessfully because of playerId is incorrect")
    public void getPlayerByIdUnsuccessfullyBecauseOfPlayerIdIsIncorrect(Long playerId, String description) {
        log.info("Testing invalid playerId case: {}", description);

        PlayerRequest playerRequest = new PlayerRequest(playerId);

        ApiResult<ErrorResponse> response =
                playerClient.getPlayerById(playerRequest, ErrorResponse.class);

        Assert.assertEquals(response.getStatus(), 400, "Expected status code is not equal actual");
        Assert.assertEquals(response.getBody().getError(), "Bad Request");
        Assert.assertEquals(response.getBody().getPath(), "/player/get");
    }


}
