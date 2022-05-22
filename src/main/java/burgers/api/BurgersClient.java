package burgers.api;

import burgers.models.BurgersIngredients;
import burgers.models.BurgersIngredientsID;
import burgers.models.BurgersUser;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class BurgersClient extends BurgersAPISpecifications {

    private static final String API_PATH = "api/";
    private static final String AUTHORISATION_PATH = API_PATH + "auth/";

    @Step("Create user")
    public ValidatableResponse createUser(BurgersUser user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(AUTHORISATION_PATH + "register")
                .then();
    }

    @Step("Login user")
    public ValidatableResponse loginUser(BurgersUser user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(AUTHORISATION_PATH + "login")
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .when()
                .delete(AUTHORISATION_PATH + "user")
                .then();
    }

    @Step("Get user")
    public ValidatableResponse getUser(String token) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .when()
                .get(AUTHORISATION_PATH + "user")
                .then();
    }

    @Step("Change user data")
    public ValidatableResponse changeDataUser(String token, BurgersUser userData) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .body(userData)
                .when()
                .patch(AUTHORISATION_PATH + "user")
                .then();
    }

    @Step("Get user data")
    public ValidatableResponse getDataUser(String token) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .when()
                .get(AUTHORISATION_PATH + "user")
                .then();
    }

    @Step("Get ingredients")
    public BurgersIngredients getAllIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(API_PATH + "ingredients")
                .as(BurgersIngredients.class);
    }

    @Step("Create order")
    public ValidatableResponse createOrder(String token, BurgersIngredientsID ingredients) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .body(ingredients)
                .when()
                .post(API_PATH + "orders")
                .then();
    }

    @Step("Get user orders")
    public ValidatableResponse getOrders(String token) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .when()
                .get(API_PATH + "orders")
                .then();
    }

    public String createUserAndReturnAccessToken(BurgersUser burgersUser) {
        String accessToken = "";
        ValidatableResponse createResponse = createUser(burgersUser);
        if (createResponse.extract().statusCode() == 200) {
            accessToken = createResponse.extract().path("accessToken");
        }
        return accessToken;
    }
}