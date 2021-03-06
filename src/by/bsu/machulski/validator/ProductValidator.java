package by.bsu.machulski.validator;

public class ProductValidator {
    private final String NAME_REGEX = ".{1,100}";
    private final String PRICE_REGEX = "[0-9]{1,8}\\.[0-9]{0,2}";
    private final String AMOUNT_REGEX = "[0-9]{1,10}";
    private final String FORM_REGEX = ".{1,100}";
    private final String FORM_DESCRIPTION_REGEX = ".{0,100}";

    public boolean checkName(String name) {
        return name.matches(NAME_REGEX);
    }

    public boolean checkPrice(String price) {
        return price.matches(PRICE_REGEX);
    }

    public boolean checkAmount(String amount) {
        return amount.matches(AMOUNT_REGEX);
    }

    public boolean checkForm(String form) {
        return form.matches(FORM_REGEX);
    }

    public boolean checkFormDescription(String formDescription) {
        return formDescription.matches(FORM_DESCRIPTION_REGEX);
    }
}
