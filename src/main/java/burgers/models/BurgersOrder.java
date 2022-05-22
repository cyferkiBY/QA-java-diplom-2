package burgers.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BurgersOrder {
    private List<BurgersIngredient> ingredients;
    private String _id;
    private BurgersOrderOwner owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;
    private int price;
}