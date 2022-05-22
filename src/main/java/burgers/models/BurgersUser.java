package burgers.models;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@Builder
public class BurgersUser {
    private String email;
    private String password;
    private String name;

    public static BurgersUser getRandomUser() {
        final String courierRandomEmail = getRandomEmail();
        final String courierRandomPassword = getRandomPassword();
        final String courierRandomName = getRandomName();

        return new BurgersUser(courierRandomEmail, courierRandomPassword, courierRandomName);
    }

    public static String getRandomEmail() {
        return (RandomStringUtils.randomAlphabetic(10) + "@gmail.com").toLowerCase();
    }

    public static String getRandomPassword() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static String getRandomName() {
        return RandomStringUtils.randomAlphabetic(15);
    }
}