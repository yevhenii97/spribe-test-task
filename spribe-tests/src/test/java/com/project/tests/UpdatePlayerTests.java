package com.project.tests;

import com.project.config.SpribeTestConfig;
import com.project.restapi.client.PlayerClient;
import com.project.restapi.models.*;
import com.project.restapi.service.PlayerValidationService;
import com.project.testdata.UpdatePlayerDataProvider;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@SpringBootTest(classes = SpribeTestConfig.class)
@Listeners({AllureTestNg.class})
@Epic("Player API")
@Feature("Update Player")
public class UpdatePlayerTests extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(UpdatePlayerTests.class);

    @Autowired
    private PlayerClient playerClient;
    @Autowired
    private PlayerValidationService playerValidationService;

    @Test(dataProvider = "validUpdateRequests", dataProviderClass = UpdatePlayerDataProvider.class)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Update player field test")
    public void updatePlayerFieldTest(UpdatePlayerRequest updatePlayerRequest, String description) {

        log.info("Testing valid player update case: {}", description);

        CreatePlayerResponse player = playerService.createBasePlayer();
        registerForCleanup(player.getId());

        GetPlayerResponse getPlayer = playerService.getPlayer(player.getId());

        ApiResult<UpdatePlayerResponse> response = playerClient.updatePlayer(
                "supervisor",
                player.getId(),
                updatePlayerRequest,
                UpdatePlayerResponse.class
        );

        Assert.assertEquals(response.getStatus(), 200, "Expected status code is not equal actual");
//       if only I had DB access, it would be easier than current validation
        playerValidationService.assertUpdatedPlayer(
                getPlayer,
                response.getBody(),
                updatePlayerRequest
        );
    }

    @Test(dataProvider = "getAge", dataProviderClass = UpdatePlayerDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Player can not be updated with invalid age range")
    public void checkThatPlayerCanNotBeUpdatedWithInvalidAgeRange(int age) {

        UpdatePlayerRequest updatePlayerRequest =
                new UpdatePlayerRequest(age, null, null, null, null, null);

        CreatePlayerResponse player = playerService.createBasePlayer();
        registerForCleanup(player.getId());

        ApiResult<ErrorResponse> response = playerClient.updatePlayer(
                "supervisor",
                player.getId(),
                updatePlayerRequest,
                ErrorResponse.class
        );

        Assert.assertEquals(response.getStatus(), 400, "Expected status code is not equal actual");
        Assert.assertEquals(response.getBody().getError(), "Bad Request");
        Assert.assertEquals(response.getBody().getPath(), "/player/update/supervisor/" + player.getId().toString());
    }

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Player can not be updated with login which already exist")
    public void checkThatPlayerCanNotBeUpdatedWithLoginWhichAlreadyExist() {
        CreatePlayerResponse player = playerService.createBasePlayer();
        CreatePlayerResponse secondPlayer = playerService.createBasePlayer();
        registerForCleanup(player.getId());
        registerForCleanup(secondPlayer.getId());

        GetPlayerResponse getFirstPlayerInfo = playerService.getPlayer(player.getId());

        UpdatePlayerRequest updatePlayerRequest =
                new UpdatePlayerRequest(null, null, getFirstPlayerInfo.getLogin(), null, null, null);

        ApiResult<ErrorResponse> response = playerClient.updatePlayer(
                "supervisor",
                secondPlayer.getId(),
                updatePlayerRequest,
                ErrorResponse.class
        );

        Assert.assertEquals(response.getStatus(), 409, "Expected status code is not equal actual");
        Assert.assertEquals(response.getBody().getError(), "Conflict");
        Assert.assertEquals(response.getBody().getPath(), "/player/update/supervisor/" + secondPlayer.getId().toString());
    }

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Player can not be updated with screenName which already exist")
    public void checkThatPlayerCanNotBeUpdatedWithScreenNameWhichAlreadyExist() {
        CreatePlayerResponse player = playerService.createBasePlayer();
        CreatePlayerResponse secondPlayer = playerService.createBasePlayer();
        registerForCleanup(player.getId());
        registerForCleanup(secondPlayer.getId());

        GetPlayerResponse getFirstPlayerInfo = playerService.getPlayer(player.getId());

        UpdatePlayerRequest updatePlayerRequest =
                new UpdatePlayerRequest(null, null, null, null, null, getFirstPlayerInfo.getScreenName());

        ApiResult<ErrorResponse> response = playerClient.updatePlayer(
                "supervisor",
                secondPlayer.getId(),
                updatePlayerRequest,
                ErrorResponse.class
        );

        Assert.assertEquals(response.getStatus(), 409, "Expected status code is not equal actual");
        Assert.assertEquals(response.getBody().getError(), "Conflict");
        Assert.assertEquals(response.getBody().getPath(), "/player/update/supervisor/" + secondPlayer.getId().toString());
    }

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Player can be updated with editor admin")
    public void checkThatPlayerCanBeUpdatedWithEditorAdmin() {
        CreatePlayerResponse player = playerService.createBasePlayer();
        registerForCleanup(player.getId());

        UpdatePlayerRequest updatePlayerRequest =
                new UpdatePlayerRequest(25, null, null, null, null, null);

        ApiResult<UpdatePlayerResponse> response = playerClient.updatePlayer(
                "admin",
                player.getId(),
                updatePlayerRequest,
                UpdatePlayerResponse.class
        );

        Assert.assertEquals(response.getStatus(), 200, "Expected status code is not equal actual");
        Assert.assertEquals(response.getBody().getAge(), 25, "Expected age is NOT equal actual");

    }
}
