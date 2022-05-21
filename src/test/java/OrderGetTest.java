import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderGetTest {

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
    @DisplayName("Check get user orders with two new order with valid token")
    public void checkGetUserOrdersWithTwoNewOrdersWithValidToken() {
        BurgersIngredients ingredients = client.getAllIngredients();

        BurgersIngredientsID burgersIngredientsID = new BurgersIngredientsID();
        burgersIngredientsID.addIngredient(ingredients.getRandomIngredient().get_id());
        burgersIngredientsID.addIngredient(ingredients.getRandomIngredient().get_id());

        String idOrder1 = createUserOrderWithBurgerIngredients(burgersIngredientsID);
        String idOrder2 = createUserOrderWithBurgerIngredients(burgersIngredientsID);

        ValidatableResponse responseGetOrders = client.getOrders(tokenUser);
        int statusCode = responseGetOrders.extract().statusCode();
        assertEquals("Не верный статус-код.", 200, statusCode);

        BurgersGetOrders orders = responseGetOrders.extract().as(BurgersGetOrders.class);
        int numberOfOrders = orders.getOrders().size();
        assertEquals("Не верное количество заказов.", 2, numberOfOrders);

        assertEquals("ID заказа не совпало", idOrder1, orders.getOrders().get(0).get_id());
        assertEquals("ID заказа не совпало", idOrder2, orders.getOrders().get(1).get_id());
    }

    @Test
    @DisplayName("Check get user orders without order with valid token")
    public void checkGetUserOrdersWithoutOrderWithValidToken() {
        ValidatableResponse responseGetOrders = client.getOrders(tokenUser);
        BurgersGetOrders orders = responseGetOrders.extract().as(BurgersGetOrders.class);
        int statusCode = responseGetOrders.extract().statusCode();
        assertEquals("Не верный статус-код.", 200, statusCode);
        int numberOfOrders = orders.getOrders().size();
        assertEquals("Не верное количество заказов.", 0, numberOfOrders);
    }

    @Test
    @DisplayName("Check get user orders without token")
    public void checkGetUserOrdersWithoutToken() {
        String emptyTokenUser = "";
        ValidatableResponse responseGetOrders = client.getOrders(emptyTokenUser);
        int statusCode = responseGetOrders.extract().statusCode();
        assertEquals("Не верный статус-код.", 401, statusCode);
    }

    @Test
    @DisplayName("Check get user orders with not valid token")
    public void checkGetUserOrdersWithNotValidToken() {
        String invalidTokenUser = "Bearer notValidToken";
        ValidatableResponse responseGetOrders = client.getOrders(invalidTokenUser);
        int statusCode = responseGetOrders.extract().statusCode();
        assertEquals("Не верный статус-код.", 403, statusCode);
    }

    private String createUserOrderWithBurgerIngredients(BurgersIngredientsID burgersIngredientsID) {
        String idOrder = "";
        ValidatableResponse responseOrder = client.createOrder(tokenUser, burgersIngredientsID);

        if (responseOrder.extract().statusCode() == 200) {
            BurgersOrderWithName burgersOrderWithName = responseOrder.extract().as(BurgersOrderWithName.class);
            idOrder = burgersOrderWithName.getOrder().get_id();
        }
        return idOrder;
    }
}