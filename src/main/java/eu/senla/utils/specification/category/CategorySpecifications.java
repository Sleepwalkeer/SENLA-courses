package eu.senla.utils.specification.category;

import eu.senla.entity.Category;
import eu.senla.entity.Category_;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Map;

public class CategorySpecifications {

    public static Specification<Category> nameLike(String name) {
        return (root, query, cb) -> cb.like(root.get(Category_.NAME), name);
    }

    public static Specification<Category> discountMoreThan(BigDecimal discount) {
        return (root, query, cb) -> cb.greaterThan(root.get(Category_.DISCOUNT), discount);
    }

    public static Specification<Category> discountLessThan(BigDecimal discount) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Category_.DISCOUNT), discount);
    }

    public static Specification<Category> createSpecificationFromFilters(Map<String, String> filterParams) {
        Specification<Category> spec = Specification.where(null);
        for (Map.Entry<String, String> entry : filterParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            switch (CategorySearchFilters.fromString(key)) {

                case NAME_LIKE -> {
                    spec = spec.and(nameLike(value));
                }
                case DISCOUNT_MORE_THAN -> {
                    spec = spec.and(discountMoreThan(new BigDecimal(value)));
                }
                case DISCOUNT_LESS_THAN -> {
                    spec = spec.and(discountLessThan(new BigDecimal(value)));
                }
            }
        }
        return spec;
    }
}
