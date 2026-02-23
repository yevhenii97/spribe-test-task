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
@Feature("Delete Player")
public class DeletePlayerTests extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DeletePlayerTests.class);

    @Autowired
    private PlayerClient playerClient;
    @Autowired
    private PlayerService playerService;

    Long randomId = ThreadLocalRandom.current().nextLong(100L, 1000L);

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that player can not be deleted by Id with user editor")
    public void checkThatPlayerCanNotBeDeletedWithUserEditor() {

        CreatePlayerResponse player = playerService.createBasePlayer();
        PlayerRequest playerRequest = new PlayerRequest(player.getId());


        playerClient.deletePlayerById(
                "user",
                playerRequest,
                204,
                Void.class
        );

        Void getPlayer =
                playerClient.getPlayerById(playerRequest, 403, Void.class)
                        .getBody();

        Assert.assertNull(getPlayer, "Player was deleted with USER editor");
    }

    @Test()
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that we can not delete player which does not exist")
    public void checkThatPlayerWhichDoesNotExistCanNotBeDeleted() {

//      If I had access to DB, I would get max id + 1, and would not use random
        PlayerRequest playerRequest = new PlayerRequest(randomId);

        ErrorResponse deleteRequest = playerClient.deletePlayerById(
                "supervisor",
                playerRequest,
                404,
                ErrorResponse.class
        ).getBody();

        Assert.assertEquals(deleteRequest.getStatus(), Integer.valueOf(404));
        Assert.assertEquals(deleteRequest.getError(), "Not Found");
        Assert.assertEquals(deleteRequest.getPath(), "/player/delete/supervisor");
    }

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that we can not delete player because invalid editor")
    public void checkThatWeCanNotDeletePlayerBecauseInvalidEditor() {
        CreatePlayerResponse player = playerService.createBasePlayer();
        PlayerRequest playerRequest = new PlayerRequest(player.getId());

        ErrorResponse deleteRequest = playerClient.deletePlayerById(
                "invalidSupervisor",
                playerRequest,
                403,
                ErrorResponse.class
        ).getBody();

        Assert.assertEquals(deleteRequest.getStatus(), Integer.valueOf(403));
        Assert.assertEquals(deleteRequest.getError(), "Forbidden");
        Assert.assertEquals(deleteRequest.getPath(), "/player/delete/invalidSupervisor");
    }

    @Test()
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that we can not delete player who is already deleted")
    public void checkThatWeCanNotDeletePlayerWhoIsAlreadyDeleted() {

        CreatePlayerResponse player = playerService.createBasePlayer();
        PlayerRequest playerRequest = new PlayerRequest(player.getId());

        playerClient.deletePlayerById(
                "supervisor",
                playerRequest,
                204,
                Void.class
        );

        ErrorResponse secondDeleteRequest = playerClient.deletePlayerById(
                "supervisor",
                playerRequest,
                404,
                ErrorResponse.class
        ).getBody();

        Assert.assertEquals(secondDeleteRequest.getStatus(), Integer.valueOf(404));
        Assert.assertEquals(secondDeleteRequest.getError(), "Not Found");
        Assert.assertEquals(secondDeleteRequest.getPath(), "/player/delete/supervisor");
    }

    @Test(dataProvider = "invalidPlayerIds", dataProviderClass = DeletePlayerDataProvider.class)
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that we can not delete player who is already deleted")
    public void checkThatWeCanNotDeletePlayerWithInvalidPlayerId(Long playerId, String description) {
        log.info("Testing invalid delete case: {}", description);

        PlayerRequest playerRequest = new PlayerRequest(playerId);

        ErrorResponse secondDeleteRequest = playerClient.deletePlayerById(
                "supervisor",
                playerRequest,
                404,
                ErrorResponse.class
        ).getBody();

        Assert.assertEquals(secondDeleteRequest.getStatus(), Integer.valueOf(404));
        Assert.assertEquals(secondDeleteRequest.getError(), "Not Found");
        Assert.assertEquals(secondDeleteRequest.getPath(), "/player/delete/supervisor");
    }
}
