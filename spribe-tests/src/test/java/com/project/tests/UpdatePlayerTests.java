package com.project.tests;

import com.project.config.SpribeTestConfig;
import com.project.restapi.client.PlayerClient;
import com.project.restapi.models.*;
import com.project.restapi.service.PlayerService;
import com.project.testdata.UpdatePlayerDataProvider;
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

@SpringBootTest(classes = SpribeTestConfig.class)
@ContextConfiguration
@DirtiesContext
@Listeners({AllureTestNg.class})
@Epic("Player API")
@Feature("Update Player")
public class UpdatePlayerTests extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(UpdatePlayerTests.class);

    @Autowired
    private PlayerClient playerClient;
    @Autowired
    private PlayerService playerService;

    @Test(dataProvider = "validUpdateRequests", dataProviderClass = UpdatePlayerDataProvider.class)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Update player field test")
    public void updatePlayerFieldTest(UpdatePlayerRequest updatePlayerRequest, String description) {

        log.info("Testing valid player update case: {}", description);

        CreatePlayerResponse player = playerService.createBasePlayer();
        registerForCleanup(player.getId());

        GetPlayerResponse getPlayer =
                playerClient.getPlayerById(new PlayerRequest(player.getId()), 200, GetPlayerResponse.class)
                        .getBody();

        UpdatePlayerResponse response = playerClient.updatePlayer(
                "supervisor",
                player.getId().toString(),
                updatePlayerRequest,
                200,
                UpdatePlayerResponse.class
        ).getBody();

//       if only I had DB access, it would be easier than current validation
        playerService.assertUpdatedPlayer(
                getPlayer,
                response,
                updatePlayerRequest,
                description
        );
    }

    @Test(dataProvider = "getAge", dataProviderClass = UpdatePlayerDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Player can not be updated with invalid age range")
    public void chackThatPlayerCanNotBeUpdatedWithInvalidAgeRange(int age) {

        UpdatePlayerRequest updatePlayerRequest =
                new UpdatePlayerRequest(age, null, null, null, null, null);

        CreatePlayerResponse player = playerService.createBasePlayer();
        registerForCleanup(player.getId());

        ErrorResponse response = playerClient.updatePlayer(
                "supervisor",
                player.getId().toString(),
                updatePlayerRequest,
                200,
                ErrorResponse.class
        ).getBody();

        Assert.assertEquals(response.getStatus(), Integer.valueOf(400));
        Assert.assertEquals(response.getError(), "Bad Request");
        Assert.assertEquals(response.getPath(), "/player/update/supervisor/" + player.getId().toString());
    }

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Player can not be updated with login which already exist")
    public void chackThatPlayerCanNotBeUpdatedWithLoginWhichAlreadyExist() {
        CreatePlayerResponse player = playerService.createBasePlayer();
        CreatePlayerResponse secondPlayer = playerService.createBasePlayer();
        registerForCleanup(player.getId());
        registerForCleanup(secondPlayer.getId());

        GetPlayerResponse getFirstPlayerInfo =
                playerClient.getPlayerById(new PlayerRequest(player.getId()), 200, GetPlayerResponse.class)
                        .getBody();

        UpdatePlayerRequest updatePlayerRequest =
                new UpdatePlayerRequest(null, null, getFirstPlayerInfo.getLogin(), null, null, null);

        ErrorResponse response = playerClient.updatePlayer(
                "supervisor",
                secondPlayer.getId().toString(),
                updatePlayerRequest,
                409,
                ErrorResponse.class
        ).getBody();

        Assert.assertEquals(response.getStatus(), Integer.valueOf(409));
        Assert.assertEquals(response.getError(), "Conflict");
        Assert.assertEquals(response.getPath(), "/player/update/supervisor/" + player.getId().toString());
    }

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Player can not be updated with screenName which already exist")
    public void chackThatPlayerCanNotBeUpdatedWithScreenNameWhichAlreadyExist() {
        CreatePlayerResponse player = playerService.createBasePlayer();
        CreatePlayerResponse secondPlayer = playerService.createBasePlayer();
        registerForCleanup(player.getId());
        registerForCleanup(secondPlayer.getId());

        GetPlayerResponse getFirstPlayerInfo =
                playerClient.getPlayerById(new PlayerRequest(player.getId()), 200, GetPlayerResponse.class)
                        .getBody();

        UpdatePlayerRequest updatePlayerRequest =
                new UpdatePlayerRequest(null, null, null, null, null, getFirstPlayerInfo.getScreenName());

        ErrorResponse response = playerClient.updatePlayer(
                "supervisor",
                secondPlayer.getId().toString(),
                updatePlayerRequest,
                409,
                ErrorResponse.class
        ).getBody();

        Assert.assertEquals(response.getStatus(), Integer.valueOf(409));
        Assert.assertEquals(response.getError(), "Conflict");
        Assert.assertEquals(response.getPath(), "/player/update/supervisor/" + player.getId().toString());
    }

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Player can be updated with editor admin")
    public void chackThatPlayerCanBeUpdatedWithEditorAdmin() {
        CreatePlayerResponse player = playerService.createBasePlayer();
        CreatePlayerResponse secondPlayer = playerService.createBasePlayer();
        registerForCleanup(player.getId());
        registerForCleanup(secondPlayer.getId());

        GetPlayerResponse getFirstPlayerInfo =
                playerClient.getPlayerById(new PlayerRequest(player.getId()), 200, GetPlayerResponse.class)
                        .getBody();

        UpdatePlayerRequest updatePlayerRequest =
                new UpdatePlayerRequest(null, null, null, null, null, getFirstPlayerInfo.getScreenName());

        ErrorResponse response = playerClient.updatePlayer(
                "admin",
                secondPlayer.getId().toString(),
                updatePlayerRequest,
                409,
                ErrorResponse.class
        ).getBody();

        Assert.assertEquals(response.getStatus(), Integer.valueOf(409));
        Assert.assertEquals(response.getError(), "Conflict");
        Assert.assertEquals(response.getPath(), "/player/update/supervisor/" + player.getId().toString());
    }


}
