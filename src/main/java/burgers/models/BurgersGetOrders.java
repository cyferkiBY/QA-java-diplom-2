package burgers.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BurgersGetOrders {
    private boolean success;
    private List<BurgersGetOrder> orders;
    private int total;
    private int totalToday;
}
