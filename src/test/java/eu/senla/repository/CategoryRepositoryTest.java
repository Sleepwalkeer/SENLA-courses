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

import java.math.BigDecimal;
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

        Long id = categoryRepository.findByName("catDaoFindById").get().getId();
        Optional<Category> categoryFromDb = categoryRepository.findById(id);
        Assertions.assertEquals(id, categoryFromDb.get().getId());
    }

    @Test
    public void updateTest() {
        Category category = Category.builder()
                .name("ctgupd")
                .build();
        categoryRepository.save(category);

        Category category2 = Category.builder()
                .id(categoryRepository.findByName("ctgupd").get().getId())
                .name("ctgupdnew")
                .build();
        Category categoryFromDb = categoryRepository.save(category2);

        Assertions.assertEquals(category2.getName(), categoryFromDb.getName());
    }

    @Test
    public void updateInvalidTest() {
        Category category = Category.builder()
                .id(1L)
                .name("ctgupdinvld")
                .build();
        categoryRepository.save(category);

        Category category2 = Category.builder()
                .id(1L)
                .name("test")
                .discount(new BigDecimal(-50))
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> categoryRepository.save(category2));
    }

    @Test
    public void CreateTest() {
        Category category = Category.builder().name("crtTst").build();
        Category categoryFromDb = categoryRepository.save(category);
        Assertions.assertEquals(category.getName(), categoryFromDb.getName());

    }

    @Test
    public void CreateInvalidTest() {
        Category category = Category.builder().discount(new BigDecimal(-50)).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(category));
    }

    @Test
    public void findByNameTest() {
        Category category = Category.builder().name("fndByNameTest").build();
        Category categoryFromDb = categoryRepository.save(category);
        Category findByNameCategory = categoryRepository.findByName("fndByNameTest").get();
        Assertions.assertEquals(categoryFromDb.getName(), findByNameCategory.getName());
    }

}