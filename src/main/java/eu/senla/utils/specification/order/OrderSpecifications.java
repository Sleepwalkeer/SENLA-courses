package eu.senla.utils.specification.order;

import eu.senla.entity.Account;
import eu.senla.entity.Account_;
import eu.senla.entity.Order;
import eu.senla.entity.Order_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class OrderSpecifications {

    public static Specification<Order> totalLessThan(BigDecimal price) {
        return (root, query, cb) -> cb.lessThan(root.get(Order_.TOTAL_PRICE), price);
    }

    public static Specification<Order> totalMoreThan(BigDecimal price) {
        return (root, query, cb) -> cb.greaterThan(root.get(Order_.TOTAL_PRICE), price);
    }

    public static Specification<Order> customerIdEquals(Long customerId) {
        return (root, query, cb) -> {
            Join<Order, Account> customerJoin = root.join(Order_.CUSTOMER, JoinType.INNER);
            return cb.equal(customerJoin.get(Account_.ID), customerId);
        };
    }

    public static Specification<Order> workerIdEquals(Long workerId) {
        return (root, query, cb) -> {
            Join<Order, Account> customerJoin = root.join(Order_.WORKER, JoinType.INNER);
            return cb.equal(customerJoin.get(Account_.ID), workerId);
        };
    }

    public static Specification<Order> startDateTimeEarlierThan(LocalDateTime startDateTime) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Order_.START_DATE_TIME), startDateTime);
    }

    public static Specification<Order> startDateTimeLaterThan(LocalDateTime startDateTime) {
        return (root, query, cb) -> cb.greaterThan(root.get(Order_.START_DATE_TIME), startDateTime);
    }

    public static Specification<Order> endDateTimeEarlierThan(LocalDateTime endDateTime) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Order_.END_DATE_TIME), endDateTime);
    }

    public static Specification<Order> endDateTimeLaterThan(LocalDateTime endDateTime) {
        return (root, query, cb) -> cb.greaterThan(root.get(Order_.END_DATE_TIME), endDateTime);
    }

    public static Specification<Order> moreItemsThan(int count) {
        return (root, query, cb) -> cb.greaterThan(cb.size(root.get(Order_.ITEMS)), count);
    }

    public static Specification<Order> lessItemsThan(int count) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(cb.size(root.get(Order_.ITEMS)), count);
    }

    public static Specification<Order> itemCount(int count) {
        return (root, query, cb) -> cb.equal(cb.size(root.get(Order_.ITEMS)), count);
    }

    public static Specification<Order> createSpecificationFromFilters(Map<String, String> filterParams) {
        Specification<Order> spec = Specification.where(null);
        for (Map.Entry<String, String> entry : filterParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            switch (OrderSearchFilters.fromString(key)) {
                case TOTAL_LESS_THAN -> spec = spec.and(totalLessThan(new BigDecimal(value)));
                case TOTAL_MORE_THAN -> spec = spec.and(totalMoreThan(new BigDecimal(value)));
                case CUSTOMER_ID_EQUALS -> spec = spec.and(customerIdEquals(Long.parseLong(value)));
                case WORKER_ID_EQUALS -> spec = spec.and(workerIdEquals(Long.parseLong(value)));
                case START_DATE_EARLIER_THAN -> spec = spec.and(startDateTimeEarlierThan(LocalDateTime.parse(value)));
                case START_DATE_LATER_THAN -> spec = spec.and(startDateTimeLaterThan(LocalDateTime.parse(value)));
                case END_DATE_EARLIER_THAN -> spec = spec.and(endDateTimeEarlierThan(LocalDateTime.parse(value)));
                case END_DATE_LATER_THAN -> spec = spec.and(endDateTimeLaterThan(LocalDateTime.parse(value)));
                case MORE_ITEMS_THAN -> spec = spec.and(moreItemsThan(Integer.parseInt(value)));
                case LESS_ITEMS_THAN -> spec = spec.and(lessItemsThan(Integer.parseInt(value)));
                case ITEM_COUNT -> spec = spec.and(itemCount(Integer.parseInt(value)));
            }
        }
        return spec;
    }
}
