import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BurgersGetOrder {
    private String _id;
    private List<String> ingredients;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;
}