package eu.senla.repository;

import eu.senla.configuration.ContextConfigurationTest;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.SecurityConfigurationTest;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.entity.Category;
import eu.senla.entity.Item;
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
@ContextConfiguration(classes = {ContextConfigurationTest.class, ServletConfigurationTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
public class ItemRepositoryTest extends ContainersEnvironment {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void findyByIdTest() {
        fillFindByIdItemDummyData();
        Item item = Item.builder()
                .id(1L).category(Category.builder().id(1L).name("Construction equipment").build())
                .name("Jackhammer").price(new BigDecimal(750)).quantity(8).build();

        Optional<Item> itemFromDb = itemRepository.findById(1L);
        Assertions.assertEquals(item.getId(), itemFromDb.get().getId());
    }

    private void fillFindByIdItemDummyData() {
        Category category = Category.builder().name("findItemDao").build();
        categoryRepository.save(category);
        Item jackhammer = Item.builder()
                .category(Category.builder().id(1L).name("findItemDao").build())
                .name("findItemDao").price(new BigDecimal(750)).quantity(8).build();
        itemRepository.save(jackhammer);
    }

    @Test
    public void updateTest() {
        fillUpdateItemDummyData();
        Optional<Item> itemOptional = itemRepository.findById(1L);
        Item item = itemOptional.get();

        item.setName("Ferrari");
        Item itemFromDb = itemRepository.save(item);
        Assertions.assertEquals(item.getName(), itemFromDb.getName());
    }

    private void fillUpdateItemDummyData() {
        Category category = Category.builder().name("updItemDao").build();
        categoryRepository.save(category);
        Item jackhammer = Item.builder()
                .category(Category.builder().id(1L).name("updItemDao").build())
                .name("updItemDao").price(new BigDecimal(750)).quantity(8).build();
        itemRepository.save(jackhammer);
    }

    @Test
    public void deleteByIdTest() {
        fillDeleteItemByIdDummyData();
        Long id = itemRepository.findByName("itemDelById15").get().getId();
        itemRepository.deleteById(id);
        Assertions.assertFalse(itemRepository.findById(id).isPresent());
    }

    private void fillDeleteItemByIdDummyData() {
        Category category = Category.builder().name("deleteByIdDaoItem").build();
        categoryRepository.save(category);

        Category category1 = Category.builder().name("deleteById2DaoItem").build();
        categoryRepository.save(category1);

        Item jackhammer = Item.builder()
                .category(Category.builder().id(1L).build())
                .name("itemDelById11").price(new BigDecimal(750)).quantity(8).build();
        itemRepository.save(jackhammer);
        Item angleGrinder = Item.builder()
                .category(Category.builder().id(1L).name("Construction equipment").build())
                .name("itemDelById12").price(new BigDecimal(600)).quantity(15).build();
        itemRepository.save(angleGrinder);

        Item twoBedApp = Item.builder()
                .category(Category.builder().id(1L).name("Real estate").build())
                .name("itemDelById13").price(new BigDecimal(3450)).quantity(2).build();
        itemRepository.save(twoBedApp);

        Item lamborghini = Item.builder()
                .category(Category.builder().id(2L).name("Vehicles").build())
                .name("itemDelById14").price(new BigDecimal(6300)).quantity(1).build();
        itemRepository.save(lamborghini);

        Item itemForDeletion = Item.builder()
                .category(Category.builder().id(1L).name("Real estate").build())
                .name("itemDelById15").price(new BigDecimal(345032)).quantity(232).build();
        itemRepository.save(itemForDeletion);
    }

    @Test
    public void addInvalidDataTest() {
        Item item = Item.builder()
                .category(Category.builder().id(3L).name("Real estate").build()).price(new BigDecimal(3450)).quantity(2).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(item));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertFalse(itemRepository.findById(-4L).isPresent());
    }
}
