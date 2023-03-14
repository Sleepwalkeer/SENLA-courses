package eu.senla.repository;

import eu.senla.configuration.ContextConfigurationTest;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.SecurityConfigurationTest;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.entity.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ContextConfigurationTest.class, ServletConfigurationTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
public class CategoryRepositoryTest extends ContainersEnvironment {
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void findyByIdTest() {
        Category dummyData = Category.builder().name("catDaoFindById").discount(0F).build();
        categoryRepository.save(dummyData);
        Category category = Category.builder().id(1L).build();

        Optional<Category> categoryFromDb = categoryRepository.findById(1L);
        Assertions.assertEquals(category.getId(), categoryFromDb.get().getId());
    }

    @Test
    public void updateTest() {
        Category category = Category.builder().id(1L).name("updatedNewestVersion").discount(0F).build();
        Category categoryFromDb = categoryRepository.save(category);
        Assertions.assertEquals(category.getName(), categoryFromDb.getName());
    }

    @Test
    public void deleteByIdTest() {
        fillDeleteByIdDummyData();
        categoryRepository.deleteById(4L);
        Assertions.assertFalse(categoryRepository.findById(4L).isPresent());
    }

    @Test
    public void addInvalidDataTest() {
        Category category = Category.builder().build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(category));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertFalse(categoryRepository.findById(-4L).isPresent());
    }

    private void fillDeleteByIdDummyData() {
        Category category = Category.builder().name("catDaoDelete").discount(0F).build();
        categoryRepository.save(category);

        Category category1 = Category.builder().name("catDaoDelete1").discount(0F).build();
        categoryRepository.save(category1);

        Category category2 = Category.builder().name("catDaoDelete2").discount(0F).build();
        categoryRepository.save(category2);

        Category category3 = Category.builder().name("catDaoDelete3").discount(0F).build();
        categoryRepository.save(category3);
    }
}