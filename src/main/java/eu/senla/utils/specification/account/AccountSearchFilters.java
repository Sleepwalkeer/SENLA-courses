package eu.senla.utils.specification.account;

import eu.senla.exception.InvalidFilterKeyException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccountSearchFilters {

    FIRST_NAME_LIKE("firstNameLike"),
    SECOND_NAME_LIKE("secondNameLike"),
    PHONE_LIKE("phoneLike"),
    EMAIL_LIKE("emailLike"),
    DISCOUNT_MORE_THAN("discountMoreThan"),
    DISCOUNT_LESS_THAN("discountLessThan");
    private final String searchFilter;

    public static AccountSearchFilters fromString(String value) {
        for (AccountSearchFilters key : AccountSearchFilters.values()) {
            if (key.searchFilter.equals(value)) {
                return key;
            }
        }
        throw new InvalidFilterKeyException("Invalid filter key: " + value);
    }
}
