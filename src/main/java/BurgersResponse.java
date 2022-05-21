import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BurgersResponse {
    public int statusCode;
    public String message;
    public String accessToken;
}