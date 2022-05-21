import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserLoginTest {
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
    @DisplayName("Check user can login with valid date")
    public void userCanLoginWithValidCredentials() {
        BurgersUser burgersUserCredential = BurgersUser
                .builder()
                .email(burgersUser.getEmail())
                .password(burgersUser.getPassword())
                .build();
        ValidatableResponse loginResponse = client.loginUser(burgersUserCredential);
        assertEquals("Не верный статус-код.", 200, loginResponse.extract().statusCode());
    }

    @Test
    @DisplayName("Check user can't login with not valid email and password")
    public void userCantLoginWithNotValidCredentials() {
        BurgersUser burgersUserCredential = BurgersUser
                .builder()
                .email("wrongEmail@gmail.com")
                .password("wrongPassword")
                .build();
        ValidatableResponse loginResponse = client.loginUser(burgersUserCredential);
        assertEquals("Не верный статус-код.", 401, loginResponse.extract().statusCode());
    }

    @Test
    @DisplayName("Check user can't login without password")
    public void userCantLoginWithoutPassword() {
        BurgersUser burgersUserCredential = BurgersUser
                .builder()
                .email(burgersUser.getEmail())
                .build();
        ValidatableResponse loginResponse = client.loginUser(burgersUserCredential);
        assertEquals("Не верный статус-код.", 401, loginResponse.extract().statusCode());
    }

    @Test
    @DisplayName("Check user can't login with empty password")
    public void userCantLoginWithEmptyPassword() {
        BurgersUser burgersUserCredential = BurgersUser
                .builder()
                .email(burgersUser.getEmail())
                .password("")
                .build();
        ValidatableResponse loginResponse = client.loginUser(burgersUserCredential);
        assertEquals("Не верный статус-код.", 401, loginResponse.extract().statusCode());
    }

    @Test
    @DisplayName("Check user can't login without email")
    public void userCantLoginWithoutEmail() {
        BurgersUser burgersUserCredential = BurgersUser
                .builder()
                .password(burgersUser.getPassword())
                .build();
        ValidatableResponse loginResponse = client.loginUser(burgersUserCredential);
        assertEquals("Не верный статус-код.", 401, loginResponse.extract().statusCode());
    }

    @Test
    @DisplayName("Check user can't login with empty email")
    public void userCantLoginWithEmptyEmail() {
        BurgersUser burgersUserCredential = BurgersUser
                .builder()
                .email("")
                .password(burgersUser.getPassword())
                .build();
        ValidatableResponse loginResponse = client.loginUser(burgersUserCredential);
        assertEquals("Не верный статус-код.", 401, loginResponse.extract().statusCode());
    }

    @Test
    @DisplayName("Check user can't login without email and password")
    public void userCantLoginWithoutEmailAndPassword() {
        BurgersUser burgersUserCredential = BurgersUser
                .builder()
                .build();
        ValidatableResponse loginResponse = client.loginUser(burgersUserCredential);
        assertEquals("Не верный статус-код.", 401, loginResponse.extract().statusCode());
    }
}