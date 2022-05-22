package burgers.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BurgersResponse {
    private int statusCode;
    private String message;
    private String accessToken;
}