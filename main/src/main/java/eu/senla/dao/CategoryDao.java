package eu.senla.dao;

import eu.senla.entities.Category;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class CategoryDao {
    private final List<Category> categories = new LinkedList<>();

    public List<Category> getCategories() {
        return categories;
    }

    public List<Category> getAll() {
        return getCategories();
    }

    public Category getById(Category passedCategory) {
        for (Category category : categories) {
            if (passedCategory.getId() == category.getId()) {
                return category;
            }
        }
        return null;
    }

    public Category update(Category passedCategory, String newName) {
        for (Category category : categories) {
            if (passedCategory.getId() == category.getId()) {
                category.setName(newName);
                return category;
            }
        }
        return null;
    }

    public Category create(Category passedCategory) {
        categories.add(passedCategory);
        return passedCategory;
    }

    public void delete(Category passedCategory) {
        for (int i = 0; i < categories.size(); i++) {
            if (passedCategory.getId() == categories.get(i).getId()) {
                categories.remove(i);
                return;
            }
        }
    }
}
