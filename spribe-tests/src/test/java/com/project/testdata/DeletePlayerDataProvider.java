package com.project.testdata;

import org.testng.annotations.DataProvider;

public class DeletePlayerDataProvider {

    @DataProvider(name = "getEditors")
    public static Object[][] getEditors() {
        return new Object[][]{
                {"supervisor"},
                {"admin"},
        };
    }

    @DataProvider(name = "invalidPlayerIds")
    public static Object[][] invalidPlayerIds() {
        return new Object[][]{
                {-1L, "Negative ID"},
                {0L, "Zero ID"},
                {null, "Null ID"},
        };
    }
}
