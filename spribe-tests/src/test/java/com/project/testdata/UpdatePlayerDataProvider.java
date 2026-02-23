package com.project.testdata;

import com.project.restapi.models.UpdatePlayerRequest;
import org.testng.annotations.DataProvider;

public class UpdatePlayerDataProvider {

    @DataProvider(name = "validUpdateRequests")
    public static Object[][] validUpdateRequests() {
        return new Object[][]{
                {new UpdatePlayerRequest(25, null, null, null, null, null), "Update age only"},
                {new UpdatePlayerRequest(null, "female", null, null, null, null), "Update gender only"},
                {new UpdatePlayerRequest(null, null, "newLogin_" + System.currentTimeMillis(), null, null, null), "Update login only"},
                {new UpdatePlayerRequest(null, null, null, "NewPass123", null, null), "Update password only"},
                {new UpdatePlayerRequest(null, null, null, null, null, "newScreen_" + System.currentTimeMillis()), "Update screenName only"},
                {new UpdatePlayerRequest(30, "female", "login_" + System.currentTimeMillis(), "NewPass123", null, "screen_" + System.currentTimeMillis()), "Update all fields"},
        };
    }

    @DataProvider(name = "getAge")
    public static Object[][] getAge() {
        return new Object[][]{
                {15},
                {61},
        };
    }
}