package com.project.tests;

import com.project.config.SpribeTestConfig;
import com.project.restapi.client.PlayerClient;
import com.project.restapi.models.*;
import com.project.testdata.CreatePlayerDataProvider;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(classes = SpribeTestConfig.class)
@Listeners({AllureTestNg.class})
@Epic("Player API")
@Feature("Create Player")
public class CreatePlayerTests extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CreatePlayerTests.class);

    @Autowired
    private PlayerClient playerClient;

    private Map<String, Object> baseParameters() {
        return new HashMap<>(Map.of(
                "age", "18",
                "gender", "male",
                "login", "login_" + UUID.randomUUID(),
                "role", "user",
                "screenName", "screen_" + UUID.randomUUID()
        ));
    }

    @Test(dataProvider = "getEditors", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that player can be crated with correct data and supervisor and admin editors")
    public void checkThatPlayerIsBeingCreatedSuccessfully(String editors) {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        Map<String, Object> parameters = baseParameters();

        ApiResult<CreatePlayerResponse> response =
                playerClient.createPlayer(editors, parameters, CreatePlayerResponse.class);

        Assert.assertEquals(response.getStatus(), 200, "Expected status code is not equal actual");
        Assert.assertNotNull(response.getBody().getId());
        registerForCleanup(response.getBody().getId());

        Assert.assertEquals(response.getBody().getAge(), parameters.get("age"), "Expected age is NOT equal actual");
        Assert.assertEquals(response.getBody().getGender(), parameters.get("gender"), "Expected gender is NOT equal actual");
        Assert.assertEquals(response.getBody().getLogin(), parameters.get("login"), "Expected login is NOT equal actual");
        Assert.assertEquals(response.getBody().getRole(), parameters.get("role"), "Expected role is NOT equal actual");
        Assert.assertEquals(response.getBody().getScreenName(), parameters.get("screenName"), "Expected screenName is NOT equal actual");
    }

    @Test(dataProvider = "getIncorrectEditors", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that player can not be crated with incorrect editor")
    public void checkThatPlayerIsNotBeingCreatedBecauseOfIncorrectEditor(String editors) {
        log.info("Thread ID: {}", Thread.currentThread().getId());

        ApiResult<Void> response =
                playerClient.createPlayer(editors, baseParameters(), Void.class);

        Assert.assertEquals(response.getStatus(), 403, "Expected status code is not equal actual");
        Assert.assertTrue(response.isEmptyBody(), "Response body should be empty");
    }

    @Test(dataProvider = "getAge", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that player can not be crated with age out of range")
    public void checkThatPlayerIsNotBeingCreatedBecauseOfAgeOutOfRange(int age) {
        log.info("Thread ID: {}", Thread.currentThread().getId());

        Map<String, Object> parameters = baseParameters();
        parameters.put("age", age);

        ApiResult<Void> response =
                playerClient.createPlayer("supervisor", parameters, Void.class);

        Assert.assertEquals(response.getStatus(), 400, "Expected status code is not equal actual");
        Assert.assertTrue(response.isEmptyBody(), "Response body should be empty");

    }

    @Test(dataProvider = "getRole", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that player can be crated with valid role")
    public void checkThatPlayerIsBeingCreatedWithValidRole(String role) {
        log.info("Thread ID: {}", Thread.currentThread().getId());

        Map<String, Object> parameters = baseParameters();
        parameters.put("role", role);

        ApiResult<CreatePlayerResponse> response =
                playerClient.createPlayer("supervisor", parameters, CreatePlayerResponse.class);

        Assert.assertEquals(response.getStatus(), 200, "Expected status code is not equal actual");
        Assert.assertNotNull(response.getBody().getId());
        registerForCleanup(response.getBody().getId());

        Assert.assertEquals(response.getBody().getAge(), parameters.get("age"), "Expected age is NOT equal actual");
        Assert.assertEquals(response.getBody().getGender(), parameters.get("gender"), "Expected gender is NOT equal actual");
        Assert.assertEquals(response.getBody().getLogin(), parameters.get("login"), "Expected login is NOT equal actual");
        Assert.assertEquals(response.getBody().getRole(), parameters.get("role"), "Expected role is NOT equal actual");
        Assert.assertEquals(response.getBody().getScreenName(), parameters.get("screenName"), "Expected screenName is NOT equal actual");

    }

    @Test()
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that player can not be crated with login that already exist")
    public void createPlayerWithDuplicateLoginTest() {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        String uniqueLogin  = "test1login_"+ System.currentTimeMillis();

        Map<String, Object> parameters = baseParameters();
        parameters.put("login", uniqueLogin);

        ApiResult<CreatePlayerResponse> createUser =
                playerClient.createPlayer("supervisor", parameters, CreatePlayerResponse.class);

        Assert.assertEquals(createUser.getStatus(), 200, "Expected status code is not equal actual");
        Assert.assertNotNull(createUser.getBody().getId());
        registerForCleanup(createUser.getBody().getId());

        Map<String, Object> secondParametersForDuplicate = Map.of(
                "age", "34",
                "editor", "supervisor",
                "gender", "female",
                "login", uniqueLogin,
                "role", "user",
                "screenName", "testScreenName_" + UUID.randomUUID()
        );

        ApiResult<Void> duplicate =  playerClient.createPlayer("supervisor", secondParametersForDuplicate, Void.class);

        Assert.assertEquals(duplicate.getStatus(), 400, "Expected status code is not equal actual");
        Assert.assertTrue(duplicate.isEmptyBody(), "Expected empty body on duplicate login");

    }

    @Test()
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that player can not be crated with screenName that already exist")
    public void createPlayerWithDuplicateScreenNameTest() {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        String uniqueScreenName  = "test1screenName_"+ System.currentTimeMillis();

        Map<String, Object> parameters = baseParameters();
        parameters.put("screenName", uniqueScreenName);

        ApiResult<CreatePlayerResponse> createUser =
                playerClient.createPlayer("supervisor", parameters, CreatePlayerResponse.class);

        Assert.assertEquals(createUser.getStatus(), 200, "Expected status code is not equal actual");
        Assert.assertNotNull(createUser.getBody().getId());
        registerForCleanup(createUser.getBody().getId());

        Map<String, Object> secondParametersForDuplicate = Map.of(
                "age", "34",
                "editor", "supervisor",
                "gender", "female",
                "login", "testLogin_" + UUID.randomUUID(),
                "role", "user",
                "screenName", uniqueScreenName
        );

        ApiResult<Void> duplicate =  playerClient.createPlayer("supervisor", secondParametersForDuplicate, Void.class);

        Assert.assertEquals(duplicate.getStatus(), 400, "Expected status code is not equal actual");
        Assert.assertTrue(duplicate.isEmptyBody(), "Expected empty body on duplicate screenName");

    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that player can not be crated with invalid password")
    public void checkThatPlayerIsNotBeingCreatedWithInvalidPassword(String password, String description) {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        log.info("Testing valid password case: {}", description);

        Map<String, Object> parameters = baseParameters();
        parameters.put("password", password);

        ApiResult<Void> response =
                playerClient.createPlayer("supervisor", parameters, Void.class);

        Assert.assertEquals(response.getStatus(), 400, "Expected status code is not equal actual");
        Assert.assertTrue(response.isEmptyBody(), "Expected empty body on invalid password");
    }

    @Test(dataProvider = "getGender", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that player can be crated with correct gender")
    public void checkThatPlayerBeingCreatedWithValidGender(String gender) {
        log.info("Thread ID: {}", Thread.currentThread().getId());

        Map<String, Object> parameters = baseParameters();
        parameters.put("gender", gender);

        ApiResult<CreatePlayerResponse> response =
                playerClient.createPlayer("supervisor", parameters, CreatePlayerResponse.class);

        Assert.assertEquals(response.getStatus(), 200, "Expected status code is not equal actual");
        Assert.assertNotNull(response.getBody().getId());
        registerForCleanup(response.getBody().getId());

        Assert.assertEquals(response.getBody().getAge(), parameters.get("age"), "Expected age is NOT equal actual");
        Assert.assertEquals(response.getBody().getGender(), parameters.get("gender"), "Expected gender is NOT equal actual");
        Assert.assertEquals(response.getBody().getLogin(), parameters.get("login"), "Expected login is NOT equal actual");
        Assert.assertEquals(response.getBody().getRole(), parameters.get("role"), "Expected role is NOT equal actual");
        Assert.assertEquals(response.getBody().getScreenName(), parameters.get("screenName"), "Expected screenName is NOT equal actual");
    }

    @Test(dataProvider = "missingRequiredFields", dataProviderClass = CreatePlayerDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that player can not be crated without required fields")
    public void checkThatPlayerIsNotBeingCreatedWithoutRequiredFields(String field, String description) {
        log.info("Thread ID: {}", Thread.currentThread().getId());
        log.info("Testing required fields case: {}", description);

        Map<String, Object> parameters = baseParameters();
        parameters.remove(field);

        ApiResult<ErrorResponse> response =
                playerClient.createPlayer("supervisor", parameters, ErrorResponse.class);

        Assert.assertEquals(response.getStatus(), 400, "Expected status code is not equal actual");
        Assert.assertEquals(response.getBody().getError(), "Bad Request");
        Assert.assertEquals(response.getBody().getPath(), "/player/create/supervisor");

    }
}
