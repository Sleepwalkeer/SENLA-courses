package eu.senla.repository;

import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ContextConfigurationTest;
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
@ContextConfiguration(classes = {ContextConfigurationTest.class})
@WebAppConfiguration
public class CategoryRepositoryTest extends ContainersEnvironment {
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void findyByIdTest() {
        Category dummyData = Category.builder()
                .name("catDaoFindById")
                .build();
        categoryRepository.save(dummyData);

        Optional<Category> categoryFromDb = categoryRepository.findById(1L);
        Assertions.assertEquals(1L, categoryFromDb.get().getId());
    }

    @Test
    public void updateTest() {
        Category category = Category.builder()
                .id(1L)
                .name("updatedNewestVersion")
                .build();
        Category categoryFromDb = categoryRepository.save(category);
        Assertions.assertEquals(category.getName(), categoryFromDb.getName());
    }

    @Test
    public void deleteByIdTest() {
        fillDeleteByIdDummyData();
        Long id = categoryRepository.findByName("catDaoDelete2").get().getId();
        categoryRepository.deleteById(id);
        Assertions.assertFalse(categoryRepository.findById(id).isPresent());
    }

    private void fillDeleteByIdDummyData() {
        Category category = Category.builder()
                .name("catDaoDelete")
                .build();
        categoryRepository.save(category);

        Category category1 = Category.builder()
                .name("catDaoDelete1")
                .build();
        categoryRepository.save(category1);

        Category category2 = Category.builder()
                .name("catDaoDelete2")
                .build();
        categoryRepository.save(category2);
    }

    @Test
    public void addInvalidDataTest() {
        Category category = Category.builder().build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(category));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertFalse(categoryRepository.findById(400L).isPresent());
    }

}