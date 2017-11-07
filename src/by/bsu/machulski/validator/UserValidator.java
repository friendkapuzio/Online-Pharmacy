package by.bsu.machulski.validator;

public class UserValidator {
    private final String NAME_REGEX = ".{2,32}";
    private final String PASSWORD_REGEX = ".{6,32}";
    private final String EMAIL_REGEX = ".+@[a-z]{1,10}\\.[a-z]{1,10}";
    private final String EMAIL_LENGTH_REGEX = ".{5,255}";
    private final String AMOUNT_REGEX = "[0-9]{1,8}\\.[0-9]{0,2}";

    public boolean checkAmount(String amount) {
        return amount.matches(AMOUNT_REGEX);
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

    public boolean checkUsername(String name) {
        return name.matches(NAME_REGEX);
    }
}
