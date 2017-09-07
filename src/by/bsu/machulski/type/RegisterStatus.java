package by.bsu.machulski.type;

public enum RegisterStatus {
    INCORRECT_NAME("incorrectName", "message.registration.incorrect_name"),
    INCORRECT_EMAIL("incorrectEmail", "message.registration.incorrect_email"),
    INCORRECT_PASSWORD("incorrectPassword", "message.registration.incorrect_password"),
    MISMATCHED_PASSWORDS("mismatchedPassword", "message.registration.mismatched_passwords"),
    EXISTING_EMAIL("existingEmail", "message.registration.existing_email"),
    ERROR("unknownError", "message.registration.error");

    private String messageAttribute;
    private String messagePath;

    RegisterStatus(String messageAttribute, String messagePath) {
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
