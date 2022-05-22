package burgers.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BurgersOrderWithName {
    private boolean success;
    private String name;
    private BurgersOrder order;
}
