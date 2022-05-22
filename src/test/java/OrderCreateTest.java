import burgers.api.BurgersClient;
import burgers.models.BurgersIngredients;
import burgers.models.BurgersIngredientsID;
import burgers.models.BurgersOrderWithName;
import burgers.models.BurgersUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class OrderCreateTest {

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
    @DisplayName("Check new order can create with two valid ingredients and valid token")
    public void checkNewOrderCanCreateWithTwoValidIngredientsAndValidToken() {
        BurgersIngredients ingredients = client.getAllIngredients();

        BurgersIngredientsID burgersIngredientsID = new BurgersIngredientsID();
        String id1IngredientExpected = ingredients.getRandomIngredient().get_id();
        String id2IngredientExpected = ingredients.getRandomIngredient().get_id();
        burgersIngredientsID.addIngredient(id1IngredientExpected);
        burgersIngredientsID.addIngredient(id2IngredientExpected);

        ValidatableResponse responseOrder = client.createOrder(tokenUser, burgersIngredientsID);
        int statusCode = responseOrder.extract().statusCode();
        assertEquals("Не верный статус-код.", 200, statusCode);

        BurgersOrderWithName orderWithName = responseOrder.extract().as(BurgersOrderWithName.class);
        int numberOfIngredients = getNumberOfIngredientsInTheOrder(orderWithName);

        assertEquals("Не верное количество ингредиентов.", 2, numberOfIngredients);
        String id1IngredientActual = orderWithName.getOrder().getIngredients().get(0).get_id();
        assertEquals("Не верный ID ингредиента.", id1IngredientExpected, id1IngredientActual);
        String id2IngredientActual = orderWithName.getOrder().getIngredients().get(1).get_id();
        assertEquals("Не верный ID ингредиента.", id2IngredientExpected, id2IngredientActual);
    }

    @Test
    @DisplayName("Check new order can't create without token")
    public void checkNewOrderCanNotCreateWithoutToken() {
        BurgersIngredients ingredients = client.getAllIngredients();

        BurgersIngredientsID burgersIngredientsID = new BurgersIngredientsID();
        String id1IngredientExpected = ingredients.getRandomIngredient().get_id();
        String id2IngredientExpected = ingredients.getRandomIngredient().get_id();
        burgersIngredientsID.addIngredient(id1IngredientExpected);
        burgersIngredientsID.addIngredient(id2IngredientExpected);

        String emptyTokenUser = "";
        ValidatableResponse responseOrder = client.createOrder(emptyTokenUser, burgersIngredientsID);
        int statusCode = responseOrder.extract().statusCode();
        assertEquals("Не верный статус-код.", 401, statusCode);
    }

    @Test
    @DisplayName("Check new order can't create with not valid token")
    public void checkNewOrderCanNotCreateWithNotValidToken() {
        BurgersIngredients ingredients = client.getAllIngredients();

        BurgersIngredientsID burgersIngredientsID = new BurgersIngredientsID();
        String id1IngredientExpected = ingredients.getRandomIngredient().get_id();
        String id2IngredientExpected = ingredients.getRandomIngredient().get_id();
        burgersIngredientsID.addIngredient(id1IngredientExpected);
        burgersIngredientsID.addIngredient(id2IngredientExpected);

        String invalidTokenUser = "Bearer notValidToken";
        ValidatableResponse responseOrder = client.createOrder(invalidTokenUser, burgersIngredientsID);
        int statusCode = responseOrder.extract().statusCode();
        assertEquals("Не верный статус-код.", 403, statusCode);
    }

    @Test
    @DisplayName("Check new order can't create without ingredients and valid token")
    public void checkNewOrderCanNotCreateWithoutIngredientsAndValidToken() {
        BurgersIngredientsID burgersIngredientsID = new BurgersIngredientsID();
        ValidatableResponse responseOrder = client.createOrder(tokenUser, burgersIngredientsID);
        int statusCode = responseOrder.extract().statusCode();
        assertEquals("Не верный статус-код.", 400, statusCode);
    }

    @Test
    @DisplayName("Check new order can't create with two not valid ingredients and valid token")
    public void checkNewOrderCanNotCreateWithTwoNotValidIngredientsAndValidToken() {
        BurgersIngredientsID burgersIngredientsID = new BurgersIngredientsID();
        String id1IngredientExpected = "not_valid1";
        String id2IngredientExpected = "not_valid2";
        burgersIngredientsID.addIngredient(id1IngredientExpected);
        burgersIngredientsID.addIngredient(id2IngredientExpected);

        ValidatableResponse responseOrder = client.createOrder(tokenUser, burgersIngredientsID);
        int statusCode = responseOrder.extract().statusCode();
        assertEquals("Не верный статус-код.", 500, statusCode);
    }

    private int getNumberOfIngredientsInTheOrder(BurgersOrderWithName orderWithName) {
        int numberOfIngredients = 0;
        try {
            numberOfIngredients = orderWithName.getOrder().getIngredients().size();
        } catch (NullPointerException thrown) {
            String message = thrown.getMessage();
        }
        return numberOfIngredients;
    }
}