import burgers.api.BurgersClient;
import burgers.models.BurgersUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserChangeTest {
    BurgersClient client;
    BurgersUser burgersUser;
    String tokenUser;

    @Before
    public void setUp() {
        client = new BurgersClient();
        burgersUser = BurgersUser.getRandomUser();
        tokenUser = client.createUserAndReturnAccessToken(burgersUser);
    }

    @After
    public void cleanData() {
        client.deleteUser(tokenUser);
    }

    @Test
    @DisplayName("Check user valid name and valid email has changed for an authorized user")
    public void checkValidNameAndEmailChangedForAuthorizedUser() {
        String newName = BurgersUser.getRandomName();
        String newEmail = BurgersUser.getRandomEmail();
        BurgersUser burgersUserNewData = BurgersUser
                .builder()
                .email(newEmail)
                .name(newName)
                .build();
        ValidatableResponse changeResponse = client.changeDataUser(tokenUser, burgersUserNewData);

        int statusCode = changeResponse.extract().statusCode();
        String message = changeResponse.extract().path("message");

        assertEquals("Не верный статус-код. " + message, 200, statusCode);
        String actualEmail = changeResponse.extract().path("user.email");
        String actualName = changeResponse.extract().path("user.name");
        assertEquals(newEmail, actualEmail);
        assertEquals(newName, actualName);
    }

    @Test
    @DisplayName("Check user valid name has changed for an authorized user")
    public void checkValidNameChangedForAuthorizedUser() {
        String newName = BurgersUser.getRandomName();
        BurgersUser burgersUserNewData = BurgersUser
                .builder()
                .name(newName)
                .build();
        ValidatableResponse changeResponse = client.changeDataUser(tokenUser, burgersUserNewData);

        int statusCode = changeResponse.extract().statusCode();
        String message = changeResponse.extract().path("message");

        assertEquals("Не верный статус-код. " + message, 200, statusCode);
        String actualName = changeResponse.extract().path("user.name");
        assertEquals(newName, actualName);
    }

    @Test
    @DisplayName("Check user valid email has changed for an authorized user")
    public void checkValidEmailChangedForAuthorizedUser() {
        String newEmail = BurgersUser.getRandomEmail();
        BurgersUser burgersUserNewData = BurgersUser
                .builder()
                .email(newEmail)
                .build();
        ValidatableResponse changeResponse = client.changeDataUser(tokenUser, burgersUserNewData);

        int statusCode = changeResponse.extract().statusCode();
        String message = changeResponse.extract().path("message");

        assertEquals("Не верный статус-код. " + message, 200, statusCode);
        String actualEmail = changeResponse.extract().path("user.email");
        assertEquals(newEmail, actualEmail);
    }

    @Test
    @DisplayName("Check name and email is not changed for not authorized user without token")
    public void checkValidNameAndEmailNotChangedForNotAuthorizedUserWithoutToken() {
        String emptyTokenUser = "";
        String newName = BurgersUser.getRandomName();
        String newEmail = BurgersUser.getRandomEmail();
        BurgersUser burgersUserNewData = BurgersUser
                .builder()
                .email(newEmail)
                .name(newName)
                .build();
        ValidatableResponse changeResponse = client.changeDataUser(emptyTokenUser, burgersUserNewData);

        int statusCode = changeResponse.extract().statusCode();
        String message = changeResponse.extract().path("message");

        assertEquals("Не верный статус-код. " + message, 401, statusCode);

        ValidatableResponse getDataResponse = client.getDataUser(tokenUser);
        String actualName = getDataResponse.extract().path("user.name");
        String actualEmail = getDataResponse.extract().path("user.email");
        assertEquals("Поле Email изменено.", burgersUser.getEmail(), actualEmail);
        assertEquals("Поле Name изменено.", burgersUser.getName(), actualName);
    }

    @Test
    @DisplayName("Check name and email is not changed for not authorized user with invalid token")
    public void checkValidNameAndEmailNotChangedForNotAuthorizedUserWithInvalidToken() {
        String invalidTokenUser = "Bearer notValidToken";
        String newName = BurgersUser.getRandomName();
        String newEmail = BurgersUser.getRandomEmail();
        BurgersUser burgersUserNewData = BurgersUser
                .builder()
                .email(newEmail)
                .name(newName)
                .build();
        ValidatableResponse changeResponse = client.changeDataUser(invalidTokenUser, burgersUserNewData);

        int statusCode = changeResponse.extract().statusCode();
        String message = changeResponse.extract().path("message");

        assertEquals("Не верный статус-код. " + message, 403, statusCode);

        ValidatableResponse getDataResponse = client.getDataUser(tokenUser);
        String actualName = getDataResponse.extract().path("user.name");
        String actualEmail = getDataResponse.extract().path("user.email");
        assertEquals("Поле Email изменено.", burgersUser.getEmail(), actualEmail);
        assertEquals("Поле Name изменено.", burgersUser.getName(), actualName);
    }
}