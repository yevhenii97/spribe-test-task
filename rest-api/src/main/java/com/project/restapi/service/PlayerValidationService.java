package com.project.restapi.service;

import com.project.restapi.models.GetPlayerResponse;
import com.project.restapi.models.UpdatePlayerRequest;
import com.project.restapi.models.UpdatePlayerResponse;
import io.qameta.allure.Step;
import org.assertj.core.api.SoftAssertions;
import org.springframework.stereotype.Service;

@Service
public class PlayerValidationService {

    @Step("Assert updated player")
    public void assertUpdatedPlayer(
            GetPlayerResponse before,
            UpdatePlayerResponse updated,
            UpdatePlayerRequest req
    ) {

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(updated.getId())
                .as("Check id")
                .isEqualTo(before.getId());

        if (req.getAge() != null) {
            softAssertions.assertThat(updated.getAge()).as("Check age").isEqualTo(req.getAge());

        } else {
            softAssertions.assertThat(updated.getAge()).as("Check age").isEqualTo(before.getAge());
        }

        if (req.getGender() != null) {
            softAssertions.assertThat(updated.getGender()).as("Check gender").isEqualTo(req.getGender());

        } else {
            softAssertions.assertThat(updated.getGender()).as("Check gender").isEqualTo(before.getGender());
        }

        if (req.getLogin() != null) {
            softAssertions.assertThat(updated.getLogin()).as("Check login").isEqualTo(req.getLogin());

        } else {
            softAssertions.assertThat(updated.getLogin()).as("Check login").isEqualTo(before.getLogin());
        }

        if (req.getScreenName() != null) {
            softAssertions.assertThat(updated.getScreenName()).as("Check screenName").isEqualTo(req.getScreenName());

        } else {
            softAssertions.assertThat(updated.getScreenName()).as("Check screenName").isEqualTo(before.getScreenName());
        }

        if (req.getRole() != null) {
            softAssertions.assertThat(updated.getRole()).as("Check role").isEqualTo(req.getRole());

        } else {
            softAssertions.assertThat(updated.getRole()).as("Check role").isEqualTo(before.getRole());
        }

        softAssertions.assertAll();
    }




}
