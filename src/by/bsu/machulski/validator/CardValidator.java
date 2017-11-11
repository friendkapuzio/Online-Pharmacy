package by.bsu.machulski.validator;

import java.math.BigDecimal;
import java.time.YearMonth;

public class CardValidator {
    private final String NUMBER_REGEX = "[0-9]{16}";
    private final String VERIFICATION_CODE_REGEX = "[0-9]{3}";
    private final String HOLDER_REGEX = "[a-zA-Z]{1,100} [a-zA-Z]{1,100}";
    private final String MONTH_REGEX = "(0?[1-9])|(1[0-2])";
    private final String YEAR_REGEX = "[0-9]{4}";

    public boolean checkNumber(String number) {
        return number.matches(NUMBER_REGEX);
    }

    public boolean checkCode(String code) {
        return code.matches(VERIFICATION_CODE_REGEX);
    }

    public boolean checkHolder(String holder) {
        return holder.matches(HOLDER_REGEX);
    }

    /**
     * Validates given pair of {@code month} and {@code year} to be correct date and checks if it equal to or later than today's.
     *
     * @param month the month of the date to be validated
     * @param year the year of the date to be validated
     * @return true if and only if {@code month} and {@code year} pair is a valid date and equal to or later than today's.
     * @throws NullPointerException if {@code month} or {@code year} is null.
     */
    public boolean checkDate(String month, String year) {
        boolean isValid = false;
        if (month.matches(MONTH_REGEX) && year.matches(YEAR_REGEX)) {
            isValid = !YearMonth.now().isAfter(YearMonth.of(Integer.parseInt(year), Integer.parseInt(month)));
        }
        return isValid;
    }

    public boolean checkMoney(BigDecimal amount) {
        return Math.random() < 0.1 ? false : true;
    }
}
