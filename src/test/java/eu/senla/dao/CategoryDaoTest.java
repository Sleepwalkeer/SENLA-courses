package eu.senla.dao;

import eu.senla.Config;
import eu.senla.entities.Category;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ContextConfiguration(classes = {Config.class})
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryDaoTest {
    @Autowired
    CategoryDao categoryDao;


    @BeforeAll
    public void setUp() {
        fillDatabaseWithDummyData();
    }

    @Test
    public void findyByIdTest() {
        Category category = Category.builder().id(1).name("Construction equipment").build();

        Optional<Category> categoryFromDb = categoryDao.findById(1);
        Assertions.assertEquals(category, categoryFromDb.get());
    }

    @Test
    public void updateTest() {
        Category category = Category.builder().id(1).name("Construction equipment").build();

        category.setName("Construction Tools");
        Category categoryFromDb = categoryDao.update(category);
        Assertions.assertEquals(category.getName(), categoryFromDb.getName());
    }

    @Test
    public void deleteByIdTest() {
        categoryDao.deleteById(4);
        Assertions.assertNull(categoryDao.findById(4));
    }

    @Test
    public void addInvalidDataTest() {
        Category category = Category.builder().name("Real estate").build();
        Assertions.assertThrows(PersistenceException.class, () -> categoryDao.save(category));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertNull(categoryDao.findById(-4));
    }

    private void fillDatabaseWithDummyData() {
        Category category = Category.builder().name("Construction equipment").build();
        categoryDao.save(category);

        Category category1 = Category.builder().name("Vehicles").build();
        categoryDao.save(category1);

        Category category2 = Category.builder().name("Real estate").build();
        categoryDao.save(category2);

        Category category3 = Category.builder().name("For deletion").build();
        categoryDao.save(category3);
    }
}