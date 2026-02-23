package com.project.restapi.service;

import com.project.restapi.models.GetPlayerResponse;
import com.project.restapi.models.UpdatePlayerRequest;
import com.project.restapi.models.UpdatePlayerResponse;
import io.qameta.allure.Step;
import org.springframework.stereotype.Service;
import org.testng.Assert;

@Service
public class PlayerValidationService {

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
