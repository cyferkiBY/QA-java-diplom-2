package burgers.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BurgersIngredient {
    private String _id;
    private String name;
    private String type;
    private int proteins;
    private int fat;
    private int carbohydrates;
    private int calories;
    private float price;
    private String image;
    private String image_mobile;
    private String image_large;
    private int __v;
}