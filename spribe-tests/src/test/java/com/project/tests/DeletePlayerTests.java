package com.project.tests;

import com.project.config.SpribeTestConfig;
import com.project.restapi.client.PlayerClient;
import com.project.restapi.models.*;
import com.project.restapi.service.PlayerService;
import com.project.testdata.DeletePlayerDataProvider;
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
@Feature("Delete Player")
public class DeletePlayerTests extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DeletePlayerTests.class);

    @Autowired
    private PlayerClient playerClient;
    @Autowired
    private PlayerService playerService;

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that player can not be deleted by Id with user editor")
    public void checkThatPlayerCanNotBeDeletedWithUserEditor() {
        CreatePlayerResponse player = playerService.createBasePlayer();
        registerForCleanup(player.getId());

        PlayerRequest playerRequest = new PlayerRequest(player.getId());

        ApiResult<Void> deletePlayer = playerClient.deletePlayerById(
                "user",
                playerRequest,
                Void.class
        );
        Assert.assertEquals(deletePlayer.getStatus(), 403, "User editor should not be able to delete");

        ApiResult<GetPlayerResponse> getPlayer =
                playerClient.getPlayerById(playerRequest, GetPlayerResponse.class);

        Assert.assertEquals(getPlayer.getStatus(), 200, "Expected status code is not equal actual");
        Assert.assertNotNull(getPlayer.getBody(), "Player was deleted with USER editor");
    }

    @Test()
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that we can not delete player which does not exist")
    public void checkThatPlayerWhichDoesNotExistCanNotBeDeleted() {

//      If I had access to DB, I would get max id + 1, and would not use random
        PlayerRequest playerRequest = new PlayerRequest(
                ThreadLocalRandom.current().nextLong(Long.MAX_VALUE / 2, Long.MAX_VALUE)
        );

        ApiResult<ErrorResponse> deleteRequest = playerClient.deletePlayerById(
                "supervisor",
                playerRequest,
                ErrorResponse.class
        );

        Assert.assertEquals(deleteRequest.getStatus(), Integer.valueOf(404));
        Assert.assertEquals(deleteRequest.getBody().getError(), "Not Found");
        Assert.assertEquals(deleteRequest.getBody().getPath(), "/player/delete/supervisor");
    }

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that we can not delete player because invalid editor")
    public void checkThatWeCanNotDeletePlayerBecauseInvalidEditor() {
        CreatePlayerResponse player = playerService.createBasePlayer();
        registerForCleanup(player.getId());

        PlayerRequest playerRequest = new PlayerRequest(player.getId());

        ApiResult<ErrorResponse> deleteRequest = playerClient.deletePlayerById(
                "invalidSupervisor",
                playerRequest,
                ErrorResponse.class
        );

        Assert.assertEquals(deleteRequest.getStatus(), Integer.valueOf(403));
        Assert.assertEquals(deleteRequest.getBody().getError(), "Forbidden");
        Assert.assertEquals(deleteRequest.getBody().getPath(), "/player/delete/invalidSupervisor");
    }

    @Test()
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that we can not delete player who is already deleted")
    public void checkThatWeCanNotDeletePlayerWhoIsAlreadyDeleted() {
        CreatePlayerResponse player = playerService.createBasePlayer();
        registerForCleanup(player.getId());

        PlayerRequest playerRequest = new PlayerRequest(player.getId());

        ApiResult<Void> deletePlayer = playerClient.deletePlayerById(
                "supervisor",
                playerRequest,
                Void.class
        );

        Assert.assertEquals(deletePlayer.getStatus(), Integer.valueOf(204));

        ApiResult<ErrorResponse> secondDeleteRequest = playerClient.deletePlayerById(
                "supervisor",
                playerRequest,
                ErrorResponse.class
        );

        Assert.assertEquals(secondDeleteRequest.getStatus(), Integer.valueOf(404));
        Assert.assertEquals(secondDeleteRequest.getBody().getError(), "Not Found");
        Assert.assertEquals(secondDeleteRequest.getBody().getPath(), "/player/delete/supervisor");
    }

    @Test(dataProvider = "invalidPlayerIds", dataProviderClass = DeletePlayerDataProvider.class)
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that we can not delete player with invalid playerId")
    public void checkThatWeCanNotDeletePlayerWithInvalidPlayerId(Long playerId, String description) {
        log.info("Testing invalid playerId case: {}", description);

        PlayerRequest playerRequest = new PlayerRequest(playerId);

        ApiResult<ErrorResponse> secondDeleteRequest = playerClient.deletePlayerById(
                "supervisor",
                playerRequest,
                ErrorResponse.class
        );

        Assert.assertEquals(secondDeleteRequest.getStatus(), Integer.valueOf(404));
        Assert.assertEquals(secondDeleteRequest.getBody().getError(), "Not Found");
        Assert.assertEquals(secondDeleteRequest.getBody().getPath(), "/player/delete/supervisor");
    }
}
