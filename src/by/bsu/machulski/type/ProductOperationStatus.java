package by.bsu.machulski.type;

public enum ProductOperationStatus {
    INCORRECT_NAME("incorrectName", "message.product.incorrect_name"),
    INCORRECT_PRICE("incorrectPrice", "message.product.incorrect_price"),
    INCORRECT_QUANTITY("incorrectQuantity", "message.product.incorrect_quantity"),
    INCORRECT_FORM("incorrectForm", "message.product.incorrect_form"),
    INCORRECT_FORM_DESCRIPTION("incorrectFormDescription", "message.product.incorrect_form_description"),
    NONEXISTENT_PRODUCT("nonexistentProduct", "message.product.nonexistent"),
    ERROR("unknownError", "message.product.error");

    private String messageAttribute;
    private String messagePath;

    ProductOperationStatus(String messageAttribute, String messagePath) {
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
