import java.util.ArrayList;
import java.util.List;

public class BurgersIngredientsID {
    private final List<String> ingredients;

    public BurgersIngredientsID() {
        this.ingredients = new ArrayList<String>();
    }

    void addIngredient(String id) {
        ingredients.add(id);
    }
}