package com.project.tests;

import com.project.restapi.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTest extends AbstractTestNGSpringContextTests {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @Autowired
    protected PlayerService playerService;

    private final ThreadLocal<List<Long>> createdPlayerIds = new ThreadLocal<>();

    @AfterMethod(alwaysRun = true)
    public void deleteCreatedPlayers() {
        List<Long> playerIds = createdPlayerIds.get();

        if (playerIds != null) {
            playerIds.forEach(playerId -> {
                log.info("CLEANUP: deleting player: {} | thread: {}",
                        playerId, Thread.currentThread().getId());
                playerService.deleteBasePlayer(playerId);
            });
            createdPlayerIds.remove();
        }
    }

    protected void registerForCleanup(Long playerId) {
        if (createdPlayerIds.get() == null) {
            createdPlayerIds.set(new ArrayList<>());
        }
        createdPlayerIds.get().add(playerId);
    }
}
