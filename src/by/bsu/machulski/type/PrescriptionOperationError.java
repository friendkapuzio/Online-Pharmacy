package by.bsu.machulski.type;

public enum PrescriptionOperationError {
    ERROR("prescriptionError", "error"),
    INCORRECT_DATE("incorrectDate", "prescription.form.incorrect_date"),
    INCORRECT_AMOUNT("incorrectAmount", "product.incorrect_quantity");

    private String messageAttribute;
    private String messagePath;

    PrescriptionOperationError(String messageAttribute, String messagePath) {
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
