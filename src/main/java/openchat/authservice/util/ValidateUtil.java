package openchat.authservice.util;

import java.util.regex.Pattern;

public class ValidateUtil {

    public Boolean validateEmail(String mail) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(regexPattern)
            .matcher(mail)
            .matches();
    }
}
