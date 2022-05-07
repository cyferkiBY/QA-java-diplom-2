import groovy.transform.builder.Builder;
import lombok.Data;

@Data
@Builder
public class BurgersOrderOwner {
    private String name;
    private String email;
    private String createdAt;
    private String updatedAt;
}