package eu.senla.dao;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.entities.Category;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
public class CategoryDaoTest extends ContainersEnvironment {
    @Autowired
    CategoryDao categoryDao;


    @Test
    public void findyByIdTest() {
        fillFindByIdDummyData();
        Category category = Category.builder().id(1).build();

        Optional<Category> categoryFromDb = categoryDao.findById(1);
        Assertions.assertEquals(category.getId(), categoryFromDb.get().getId());
    }
    private void fillFindByIdDummyData() {
        Category category = Category.builder().name("catDaoFindById").build();
        categoryDao.save(category);
    }

    @Test
    public void updateTest() {
        Category category = Category.builder().id(1).name("updatedNewestVersion").build();
        Category categoryFromDb = categoryDao.update(category);
        Assertions.assertEquals(category.getName(), categoryFromDb.getName());
    }

    private void fillUpdateDummyData() {
        Category category = Category.builder().name("catDaoUpdate").build();
        categoryDao.save(category);
    }

    @Test
    public void deleteByIdTest() {
        fillDeleteByIdDummyData();
        categoryDao.deleteById(4);
        Assertions.assertFalse(categoryDao.findById(4).isPresent());
    }

    @Test
    public void addInvalidDataTest() {
        Category category = Category.builder().build();
        Assertions.assertThrows(PersistenceException.class, () -> categoryDao.save(category));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertFalse(categoryDao.findById(-4).isPresent());
    }

    private void fillDeleteByIdDummyData() {
        Category category = Category.builder().name("catDaoDelete").build();
        categoryDao.save(category);

        Category category1 = Category.builder().name("catDaoDelete1").build();
        categoryDao.save(category1);

        Category category2 = Category.builder().name("catDaoDelete2").build();
        categoryDao.save(category2);

        Category category3 = Category.builder().name("catDaoDelete3").build();
        categoryDao.save(category3);
    }
}