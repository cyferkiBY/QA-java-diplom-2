import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class UserCreateTest {
    BurgersClient client;
    HashSet<String> usersTokensSetForDelete = new HashSet<>();

    @Before
    public void setUp() {
        client = new BurgersClient();
    }

    @After
    public void cleanData() {
        for (String accessToken : usersTokensSetForDelete) {
            client.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Check new user can create with valid date")
    public void checkNewUserCanCreateWithValidData() {
        BurgersUser burgersUser = BurgersUser.getRandomUser();
        BurgersResponse burgersResponse = createUserAndAddToTokensSet(burgersUser);
        assertEquals("Не верный статус-код. " + burgersResponse.message, 200, burgersResponse.statusCode);
    }

    @Test
    @DisplayName("Check can't create two identical users")
    public void checkCanNotCreateTwoIdenticalUsers() {
        BurgersUser burgersUser = BurgersUser.getRandomUser();
        createUserAndAddToTokensSet(burgersUser);
        BurgersResponse burgersResponse = createUserAndAddToTokensSet(burgersUser);
        assertEquals("Не верный статус-код. " + burgersResponse.message, 403, burgersResponse.statusCode);
    }

    @Test
    @DisplayName("Check new user can't create with repeated email")
    public void checkNewUserCanNotCreateWithRepeatedLogin() {
        BurgersUser burgersUser = BurgersUser.getRandomUser();
        createUserAndAddToTokensSet(burgersUser);
        burgersUser.setName("NewName");
        burgersUser.setPassword("NewPassword");
        BurgersResponse burgersResponse = createUserAndAddToTokensSet(burgersUser);
        assertEquals("Не верный статус-код. " + burgersResponse.message, 403, burgersResponse.statusCode);
    }

    @Test
    @DisplayName("Check new user can't create without password")
    public void checkNewUserCanNotCreateWithoutPassword() {
        BurgersUser burgersUser = BurgersUser.getRandomUser();
        burgersUser.setPassword("");
        BurgersResponse burgersResponse = createUserAndAddToTokensSet(burgersUser);
        assertEquals("Не верный статус-код. " + burgersResponse.message, 403, burgersResponse.statusCode);
    }

    @Test
    @DisplayName("Check new user can't create without email")
    public void checkNewUserCanNotCreateWithoutEmail() {
        BurgersUser burgersUser = BurgersUser.getRandomUser();
        burgersUser.setEmail("");
        BurgersResponse burgersResponse = createUserAndAddToTokensSet(burgersUser);
        assertEquals("Не верный статус-код. " + burgersResponse.message, 403, burgersResponse.statusCode);
    }

    @Test
    @DisplayName("Check new user can't create without name")
    public void checkNewUserCanNotCreateWithoutName() {
        BurgersUser burgersUser = BurgersUser.getRandomUser();
        burgersUser.setName("");
        BurgersResponse burgersResponse = createUserAndAddToTokensSet(burgersUser);
        assertEquals("Не верный статус-код. " + burgersResponse.message, 403, burgersResponse.statusCode);
    }

    private BurgersResponse createUserAndAddToTokensSet(BurgersUser burgersUser) {
        String message;
        String accessToken = "";
        ValidatableResponse createResponse = client.createUser(burgersUser);
        int statusCode = createResponse.extract().statusCode();

        try {
            message = createResponse.extract().path("message");
        } catch (ClassCastException thrown) {
            message = "";
        }

        if (statusCode == 200) {
            accessToken = createResponse.extract().path("accessToken");
            usersTokensSetForDelete.add(accessToken);
        }
        return new BurgersResponse(statusCode, message, accessToken);
    }
}