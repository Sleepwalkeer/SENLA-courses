package eu.senla.utils.specification.item;

import eu.senla.entity.Category;
import eu.senla.entity.Category_;
import eu.senla.entity.Item;
import eu.senla.entity.Item_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Map;

public class ItemSpecifications {

    public static Specification<Item> priceLessThan(BigDecimal price) {
        return (root, query, cb) -> cb.lessThan(root.get(Item_.PRICE), price);
    }

    public static Specification<Item> priceMoreThan(BigDecimal price) {
        return (root, query, cb) -> cb.greaterThan(root.get(Item_.PRICE), price);
    }

    public static Specification<Item> categoryEquals(String category) {
        return (root, query, cb) -> {
            Join<Item, Category> categoryJoin = root.join(Item_.CATEGORY, JoinType.INNER);
            return cb.equal(categoryJoin.get(Category_.NAME), category);
        };
    }

    public static Specification<Item> nameLike(String name) {
        return (root, query, cb) -> cb.like(root.get(Item_.NAME), name);
    }

    public static Specification<Item> discountMoreThan(BigDecimal discount) {
        return (root, query, cb) -> cb.greaterThan(root.get(Item_.DISCOUNT), discount);
    }

    public static Specification<Item> discountLessThan(BigDecimal discount) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Item_.DISCOUNT), discount);
    }

    public static Specification<Item> createSpecificationFromFilters(Map<String, String> filterParams) {
        Specification<Item> spec = Specification.where(null);
        for (Map.Entry<String, String> entry : filterParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            switch (ItemSearchFilters.fromString(key)) {
                case PRICE_LESS_THAN -> spec = spec.and(priceLessThan(new BigDecimal(value)));
                case PRICE_MORE_THAN -> spec = spec.and(priceMoreThan(new BigDecimal(value)));
                case CATEGORY_EQUALS -> spec = spec.and(categoryEquals(value));
                case NAME_LIKE -> spec = spec.and(nameLike(value));
                case DISCOUNT_MORE_THAN -> spec = spec.and(discountMoreThan(new BigDecimal(value)));
                case DISCOUNT_LESS_THAN -> spec = spec.and(discountLessThan(new BigDecimal(value)));
            }
        }
        return spec;
    }
}
