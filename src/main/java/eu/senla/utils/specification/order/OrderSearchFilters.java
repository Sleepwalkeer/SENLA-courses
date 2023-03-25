package eu.senla.utils.specification.order;

import eu.senla.exception.InvalidFilterKeyException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderSearchFilters {

    TOTAL_LESS_THAN("totalLessThan"),
    TOTAL_MORE_THAN("totalMoreThan"),
    CUSTOMER_ID_EQUALS("customerIdEquals"),
    WORKER_ID_EQUALS("workerIdEquals"),
    START_DATE_EARLIER_THAN("startDateEarlierThan"),
    START_DATE_LATER_THAN("startDateLaterThan"),
    END_DATE_EARLIER_THAN("endDateEarlierThan"),
    END_DATE_LATER_THAN("endDateLaterThan"),
    MORE_ITEMS_THAN("moreItemsThan"),
    LESS_ITEMS_THAN("lessItemsThan"),
    ITEM_COUNT("itemCount");

    private final String searchFilter;

    public static OrderSearchFilters fromString(String value) {
        for (OrderSearchFilters key : OrderSearchFilters.values()) {
            if (key.searchFilter.equals(value)) {
                return key;
            }
        }
        throw new InvalidFilterKeyException("Invalid filter key: " + value);
    }
}
