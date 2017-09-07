package by.bsu.machulski.util;

public class UserValidator {
    private final String NAME_REGEX = ".{2,32}";
    private final String PASSWORD_REGEX = ".{6,32}";
    private final String EMAIL_REGEX = ".+@[a-z]{1,10}\\.[a-z]{1,10}";
    private final String EMAIL_LENGTH_REGEX = ".{5,255}";

    public boolean checkUsername(String name) {
        return name.matches(NAME_REGEX);
    }

    public boolean checkEmail(String email) {
        return email.matches(EMAIL_LENGTH_REGEX) && email.matches(EMAIL_REGEX);
    }

    public boolean checkPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public boolean checkPasswordConformation(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
