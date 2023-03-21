package eu.senla.utils.specification.item;

import eu.senla.exception.InvalidFilterKeyException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ItemSearchFilters {

    PRICE_LESS_THAN("priceLessThan"),
    PRICE_MORE_THAN("priceMoreThan"),
    CATEGORY_EQUALS("categoryEquals"),
    NAME_LIKE("nameLike"),
    DISCOUNT_MORE_THAN("discountMoreThan"),
    DISCOUNT_LESS_THAN("discountLessThan");

    private final String searchFilter;

    public static ItemSearchFilters fromString(String value) {
        for (ItemSearchFilters key : ItemSearchFilters.values()) {
            if (key.searchFilter.equals(value)) {
                return key;
            }
        }
        throw new InvalidFilterKeyException("Invalid filter key: " + value);
    }
}
