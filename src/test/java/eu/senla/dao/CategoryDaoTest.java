package eu.senla.dao;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.TestContainers;
import eu.senla.entities.Category;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryDaoTest extends ContainersEnvironment {
    @Autowired
    CategoryDao categoryDao;


    @Test
    @Order(1)
    public void findyByIdTest() {
        fillWithDummyData();
        Category category = Category.builder().id(1).name("Construction equipment").build();

        Optional<Category> categoryFromDb = categoryDao.findById(1);
        Assertions.assertEquals(category, categoryFromDb.get());
    }

    @Test
    @Transactional
    public void updateTest() {
        Category category = Category.builder().id(1).name("Construction equipment").build();

        category.setName("Construction Tools");
        Category categoryFromDb = categoryDao.update(category);
        Assertions.assertEquals(category.getName(), categoryFromDb.getName());
    }

    @Test
    @Transactional
    public void deleteByIdTest() {
        categoryDao.deleteById(1);
        Assertions.assertFalse(categoryDao.findById(1).isPresent());
    }

    @Test
    public void addInvalidDataTest() {
        Category category = Category.builder().name("Real estate").build();
        Assertions.assertThrows(PersistenceException.class, () -> categoryDao.save(category));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertFalse(categoryDao.findById(-4).isPresent());
    }

    private void fillWithDummyData() {
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