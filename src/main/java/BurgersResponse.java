public class BurgersResponse {
    public int statusCode;
    public String message;
    public String accessToken;

    public BurgersResponse(int statusCode, String message, String accessToken) {
        this.statusCode = statusCode;
        this.message = message;
        this.accessToken = accessToken;
    }
}