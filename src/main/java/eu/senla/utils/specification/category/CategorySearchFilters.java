package eu.senla.utils.specification.category;

import eu.senla.exception.InvalidFilterKeyException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategorySearchFilters {

    NAME_LIKE("nameLike"),
    DISCOUNT_MORE_THAN("discountMoreThan"),
    DISCOUNT_LESS_THAN("discountLessThan");

    private final String searchFilter;

    public static CategorySearchFilters fromString(String value) {
        for (CategorySearchFilters key : CategorySearchFilters.values()) {
            if (key.searchFilter.equals(value)) {
                return key;
            }
        }
        throw new InvalidFilterKeyException("Invalid filter key: " + value);
    }
}
