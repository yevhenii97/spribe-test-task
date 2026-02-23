package com.project.tests;

import com.project.config.SpribeTestConfig;
import com.project.restapi.client.PlayerClient;
import com.project.restapi.models.*;
import com.project.restapi.service.PlayerService;
import com.project.testdata.CreatePlayerDataProvider;
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

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = SpribeTestConfig.class)
@ContextConfiguration
@DirtiesContext
@Listeners({AllureTestNg.class})
@Epic("Player API")
@Feature("Create Player")
public class CreatePlayerTests extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CreatePlayerTests.class);

    @Autowired
    private PlayerClient playerClient;

    @Test(dataProvider = "getEditors", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that player can be crated with correct data and supervisor and admin editors")
    public void checkThatPlayerIsBeingCreatedSuccessfully(String editors) {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        Map<String, Object> parameters = Map.of(
                "age", "17",
                "editor", "supervisor",
                "gender", "male",
                "login", "123456",
                "role", "user",
                "screenName", "test1"
        );

        CreatePlayerResponse response =
                playerClient.createPlayer(editors, parameters, 200, CreatePlayerResponse.class)
                        .getBody();

        Assert.assertNotNull(response.getId());
        registerForCleanup(response.getId());

        Assert.assertEquals(response.getAge(), parameters.get("age"), "Expected age is NOT equal actual");
        Assert.assertEquals(response.getGender(), parameters.get("gender"), "Expected gender is NOT equal actual");
        Assert.assertEquals(response.getLogin(), parameters.get("login"), "Expected login is NOT equal actual");
        Assert.assertEquals(response.getRole(), parameters.get("role"), "Expected role is NOT equal actual");
        Assert.assertEquals(response.getScreenName(), parameters.get("screenName"), "Expected screenName is NOT equal actual");
    }

    @Test(dataProvider = "getIncorrectEditors", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that player can not be crated with incorrect editor")
    public void checkThatPlayerIsNotBeingCreatedBecauseOfIncorrectEditor(String editors) {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        Map<String, Object> parameters = Map.of(
                "age", "17",
                "editor", "supervisor",
                "gender", "male",
                "login", "123456",
                "role", "user",
                "screenName", "test1"
        );

        ApiResult<Void> response =
                playerClient.createPlayer(editors, parameters, 403, Void.class);

        Assert.assertTrue(response.isEmptyBody(), "Response body should be empty");
    }

    @Test(dataProvider = "getAge", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that player can not be crated with age out of range")
    public void checkThatPlayerIsNotBeingCreatedBecauseOfAgeOutOfRange(int age) {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        Map<String, Object> parameters = Map.of(
                "age", age,
                "editor", "supervisor",
                "gender", "male",
                "login", "123456",
                "role", "user",
                "screenName", "test1"
        );

        ApiResult<Void> response =
                playerClient.createPlayer("supervisor", parameters, 400, Void.class);

        Assert.assertTrue(response.isEmptyBody(), "Response body should be empty");

    }

    @Test(dataProvider = "getRole", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that player can not crated with valid role")
    public void checkThatPlayerIsBeingCreatedWithValidRole(String role) {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        Map<String, Object> parameters = Map.of(
                "age", "18",
                "editor", "supervisor",
                "gender", "male",
                "login", "123456",
                "role", role,
                "screenName", "test1"
        );

        CreatePlayerResponse response =
                playerClient.createPlayer("supervisor", parameters, 200, CreatePlayerResponse.class)
                        .getBody();

        Assert.assertNotNull(response.getId());
        registerForCleanup(response.getId());

        Assert.assertEquals(response.getAge(), parameters.get("age"), "Expected age is NOT equal actual");
        Assert.assertEquals(response.getGender(), parameters.get("gender"), "Expected gender is NOT equal actual");
        Assert.assertEquals(response.getLogin(), parameters.get("login"), "Expected login is NOT equal actual");
        Assert.assertEquals(response.getRole(), parameters.get("role"), "Expected role is NOT equal actual");
        Assert.assertEquals(response.getScreenName(), parameters.get("screenName"), "Expected screenName is NOT equal actual");

    }

    @Test()
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that player can not be crated with login that already exist")
    public void createPlayerWithDuplicateLoginTest() {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        String uniqueLogin  = "test1login_"+ System.currentTimeMillis();

        Map<String, Object> parameters = Map.of(
                "age", "18",
                "editor", "supervisor",
                "gender", "male",
                "login", uniqueLogin,
                "role", "user",
                "screenName", "test1"
        );

        CreatePlayerResponse createUser =
                playerClient.createPlayer("supervisor", parameters, 200, CreatePlayerResponse.class)
                        .getBody();

        Assert.assertNotNull(createUser.getId());
        registerForCleanup(createUser.getId());

        Map<String, Object> secondParametersForDuplicate = Map.of(
                "age", "34",
                "editor", "supervisor",
                "gender", "female",
                "login", uniqueLogin,
                "role", "user",
                "screenName", "test2"
        );

        ApiResult<Void> duplicate =  playerClient.createPlayer("supervisor", secondParametersForDuplicate, 400, Void.class);

        Assert.assertTrue(duplicate.isEmptyBody(), "Expected empty body on duplicate login");

    }

    @Test()
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that player can not be crated with screenName that already exist")
    public void createPlayerWithDuplicateScreenNameTest() {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        String uniqueScreenName  = "test1screenName_"+ System.currentTimeMillis();

        Map<String, Object> parameters = Map.of(
                "age", "18",
                "editor", "supervisor",
                "gender", "male",
                "login", "test1login",
                "role", "user",
                "screenName", uniqueScreenName
        );

        CreatePlayerResponse createUser =
                playerClient.createPlayer("supervisor", parameters, 200, CreatePlayerResponse.class)
                        .getBody();

        Assert.assertNotNull(createUser.getId());
        registerForCleanup(createUser.getId());

        Map<String, Object> secondParametersForDuplicate = Map.of(
                "age", "34",
                "editor", "supervisor",
                "gender", "female",
                "login", "test2login",
                "role", "user",
                "screenName", uniqueScreenName
        );

        ApiResult<Void> duplicate =  playerClient.createPlayer("supervisor", secondParametersForDuplicate, 400, Void.class);

        Assert.assertTrue(duplicate.isEmptyBody(), "Expected empty body on duplicate screenName");

    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that player can not be crated with invalid password")
    public void checkThatPlayerIsNotBeingCreatedWithInvalidPassword(String password, String description) {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        log.info("Testing valid password case: {}", description);

        Map<String, Object> parameters = Map.of(
                "age", "18",
                "editor", "supervisor",
                "gender", "male",
                "login", "123456",
                "role", "user",
                "screenName", "test1",
                "password", password
        );

        ApiResult<Void> response =
                playerClient.createPlayer("supervisor", parameters, 400, Void.class);

        Assert.assertTrue(response.isEmptyBody(), "Expected empty body on invalid password");
    }

    @Test(dataProvider = "getGender", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that player can be crated with correct gender")
    public void checkThatPlayerNotBeingCreatedWithValidGender(String gender) {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        Map<String, Object> parameters = Map.of(
                "age", "17",
                "editor", "supervisor",
                "gender", gender,
                "login", "123456",
                "role", "user",
                "screenName", "test1"
        );

        CreatePlayerResponse response =
                playerClient.createPlayer("supervisor", parameters, 200, CreatePlayerResponse.class)
                        .getBody();

        Assert.assertNotNull(response.getId());
        registerForCleanup(response.getId());

        Assert.assertEquals(response.getAge(), parameters.get("age"), "Expected age is NOT equal actual");
        Assert.assertEquals(response.getGender(), parameters.get("gender"), "Expected gender is NOT equal actual");
        Assert.assertEquals(response.getLogin(), parameters.get("login"), "Expected login is NOT equal actual");
        Assert.assertEquals(response.getRole(), parameters.get("role"), "Expected role is NOT equal actual");
        Assert.assertEquals(response.getScreenName(), parameters.get("screenName"), "Expected screenName is NOT equal actual");
    }

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that player can not be crated with incorrect editor")
    public void checkThatPlayerIsNotBeingCreatedBecauseOfInvalidEditor() {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        Map<String, Object> parameters = Map.of(
                "age", 19,
                "editor", "user",
                "gender", "male",
                "login", "123456",
                "role", "user",
                "screenName", "test1"
        );

        ApiResult<Void> response =
                playerClient.createPlayer("user", parameters, 403, Void.class);

        Assert.assertTrue(response.isEmptyBody(), "Response body should be empty");
    }

    @Test(dataProvider = "missingRequiredFields", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that player can not be crated without required fields")
    public void checkThatPlayerIsNotBeingCreatedWithoutRequiredFields(String field, String description) {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        log.info("Testing required fields case: {}", description);

        Map<String, Object> parameters = new HashMap<>(
                Map.of(
                "age", 19,
                "editor", "supervisor",
                "gender", "male",
                "login", "123456",
                "role", "user",
                "screenName", "test1"
        ));

        parameters.remove(field);

        ErrorResponse response =
                playerClient.createPlayer("supervisor", parameters, 400, ErrorResponse.class).getBody();

        Assert.assertEquals(response.getStatus(), Integer.valueOf(400));
        Assert.assertEquals(response.getError(), "Bad Request");
        Assert.assertEquals(response.getPath(), "/player/create/supervisor");

    }
}
