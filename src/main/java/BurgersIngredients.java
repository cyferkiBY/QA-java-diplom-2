import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Random;

@Builder
@Data
public class BurgersIngredients {
    private boolean success;
    private List<BurgersIngredient> data;

    public BurgersIngredient getRandomIngredient() {
        BurgersIngredient ingredient = BurgersIngredient.builder().build();
        if (data.size() > 0) {
            Random random = new Random();
            int index = random.nextInt(data.size());
            ingredient = data.get(index);
        }
        return ingredient;
    }
}