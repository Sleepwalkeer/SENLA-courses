package eu.senla.repository;

import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ContextConfigurationTest;
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
@ContextConfiguration(classes = {ContextConfigurationTest.class})
@WebAppConfiguration
public class ItemRepositoryTest extends ContainersEnvironment {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void findyByIdTest() {
        fillFindByIdItemDummyData();

        Optional<Item> itemFromDb = itemRepository.findById(1L);
        Assertions.assertEquals(1L, itemFromDb.get().getId());
    }

    private void fillFindByIdItemDummyData() {
        Category category = Category.builder()
                .name("findItemDao")
                .build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .name("findItemDao")
                .price(new BigDecimal(750))
                .quantity(8)
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
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
        Category category = Category.builder()
                .name("updItemDao")
                .build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .name("updItemDao")
                .price(new BigDecimal(750))
                .quantity(8)
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
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
        Category category = Category.builder()
                .name("deleteByIdDaoItem")
                .build();
        categoryRepository.save(category);

        Item itemForDeletion = Item.builder()
                .name("itemDelById15")
                .price(new BigDecimal(345032))
                .quantity(232)
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(itemForDeletion);
    }

    @Test
    public void addInvalidDataTest() {
        Item item = Item.builder()
                .price(new BigDecimal(3450)).quantity(2).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(item));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertFalse(itemRepository.findById(600L).isPresent());
    }
}
