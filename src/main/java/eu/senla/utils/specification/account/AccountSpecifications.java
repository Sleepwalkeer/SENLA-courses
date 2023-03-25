package eu.senla.utils.specification.account;

import eu.senla.entity.Account;
import eu.senla.entity.Account_;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Map;

public class AccountSpecifications {

    public static Specification<Account> firstNameLike(String name) {
        return (root, query, cb) -> cb.like(root.get(Account_.FIRST_NAME), name);
    }

    public static Specification<Account> secondNameLike(String name) {
        return (root, query, cb) -> cb.like(root.get(Account_.SECOND_NAME), name);
    }

    public static Specification<Account> phoneLike(String name) {
        return (root, query, cb) -> cb.like(root.get(Account_.PHONE), name);
    }

    public static Specification<Account> emailLike(String name) {
        return (root, query, cb) -> cb.like(root.get(Account_.EMAIL), name);
    }

    public static Specification<Account> discountMoreThan(BigDecimal discount) {
        return (root, query, cb) -> cb.greaterThan(root.get(Account_.DISCOUNT), discount);
    }

    public static Specification<Account> discountLessThan(BigDecimal discount) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Account_.DISCOUNT), discount);
    }

    public static Specification<Account> createSpecificationFromFilters(Map<String, String> filterParams) {
        Specification<Account> spec = Specification.where(null);
        for (Map.Entry<String, String> entry : filterParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            switch (AccountSearchFilters.fromString(key)) {

                case FIRST_NAME_LIKE -> spec = spec.and(firstNameLike(value));
                case SECOND_NAME_LIKE -> spec = spec.and(secondNameLike(value));
                case PHONE_LIKE -> spec = spec.and(phoneLike(value));
                case EMAIL_LIKE -> spec = spec.and(emailLike(value));
                case DISCOUNT_MORE_THAN -> spec = spec.and(discountMoreThan(new BigDecimal(value)));
                case DISCOUNT_LESS_THAN -> spec = spec.and(discountLessThan(new BigDecimal(value)));
            }
        }
        return spec;
    }
}
