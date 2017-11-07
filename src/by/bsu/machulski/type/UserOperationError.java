package by.bsu.machulski.type;

public enum UserOperationError {
    BLOCKED_USER("incorrectUser", "user.blocked"),
    ERROR("userError", "error"),
    EXISTING_EMAIL("existingEmail", "user.existing_email"),
    INCORRECT_AMOUNT("incorrectAmount", "user.incorrect_amount"),
    INCORRECT_CARD_INFORMATION("cardError", "user.incorrect_card"),
    INCORRECT_EMAIL("incorrectEmail", "user.incorrect_email"),
    INCORRECT_NAME("incorrectName", "user.incorrect_name"),
    INCORRECT_NEW_PASSWORD("incorrectNewPassword", "user.incorrect_password"),
    INCORRECT_PASSWORD("incorrectPassword", "user.incorrect_password"),
    INSUFFICIENT_FUNDS("cardError","user.insufficient_funds"),
    MISMATCHED_PASSWORDS("mismatchedPassword", "user.mismatched_passwords"),
    NONEXISTENT_USER("incorrectUser", "user.nonexistent"),
    WRONG_PASSWORD("incorrectPassword", "user.wrong_password");

    private String messageAttribute;
    private String messagePath;

    UserOperationError(String messageAttribute, String messagePath) {
        this.messageAttribute = messageAttribute;
        this.messagePath = messagePath;
    }

    public String getMessageAttribute() {
        return messageAttribute;
    }

    public String getMessagePath() {
        return messagePath;
    }
}
