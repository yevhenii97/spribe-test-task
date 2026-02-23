package com.project.testdata;

import org.testng.annotations.DataProvider;

public class CreatePlayerDataProvider {

    @DataProvider(name = "getEditors")
    public static Object[][] getEditors() {
        return new Object[][]{
                {"supervisor"},
                {"admin"},
        };
    }

    @DataProvider(name = "getIncorrectEditors")
    public static Object[][] getIncorrectEditors() {
        return new Object[][]{
                {"user"},
                {"qwerty"},
                {"123456$$#"},
        };
    }

    @DataProvider(name = "getAge")
    public static Object[][] getAge() {
        return new Object[][]{
                {15},
                {61},
        };
    }

    @DataProvider(name = "getRole")
    public static Object[][] getRole() {
        return new Object[][]{
                {"admin"},
                {"user"},
        };
    }

    @DataProvider(name = "getGender")
    public static Object[][] getGender() {
        return new Object[][]{
                {"male"},
                {"female"},
        };
    }

    @DataProvider(name = "invalidPasswords")
    public static Object[][] invalidPasswords() {
        return new Object[][]{
                {"Ab1234", "Password is too short (6 chars)"},
                {"Ab123456789012345", "Password is too long (17 chars)"},
                {"AbcdefGh", "Password without numbers"},
                {"12345678", "Password without letters"},
                {"Ab!@#$%^", "Password with special characters"},
                {"Ab 12345", "Password with spaces"},
                {"", "Empty password"},
        };
    }

    @DataProvider(name = "missingRequiredFields")
    public static Object[][] missingRequiredFields() {
        return new Object[][]{
                {"age", "Missing age"},
                {"editor", "Missing editor"},
                {"gender", "Missing gender"},
                {"login", "Missing login"},
                {"role", "Missing role"},
                {"screenName", "Missing screenName"},
        };
    }
}
