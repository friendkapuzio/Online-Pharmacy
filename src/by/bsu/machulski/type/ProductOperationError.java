package by.bsu.machulski.type;

public enum ProductOperationError {
    ERROR("productError", "error"),
    INCORRECT_NAME("incorrectName", "product.incorrect_name"),
    INCORRECT_PRICE("incorrectPrice", "product.incorrect_price"),
    INCORRECT_QUANTITY("incorrectQuantity", "product.incorrect_quantity"),
    INCORRECT_FORM("incorrectForm", "product.incorrect_form"),
    INCORRECT_FORM_DESCRIPTION("incorrectFormDescription", "product.incorrect_form_description");

    private String messageAttribute;
    private String messagePath;

    ProductOperationError(String messageAttribute, String messagePath) {
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
