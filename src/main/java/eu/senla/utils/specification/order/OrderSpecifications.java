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
}
