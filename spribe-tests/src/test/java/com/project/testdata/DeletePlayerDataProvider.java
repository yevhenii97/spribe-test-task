package com.project.testdata;

import org.testng.annotations.DataProvider;

public class DeletePlayerDataProvider {

    @DataProvider(name = "invalidPlayerIds")
    public static Object[][] invalidPlayerIds() {
        return new Object[][]{
                {-1L, "Negative ID"},
                {0L, "Zero ID"},
                {null, "Null ID"},
        };
    }
}
