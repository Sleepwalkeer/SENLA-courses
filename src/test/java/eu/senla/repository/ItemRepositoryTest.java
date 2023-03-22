package eu.senla.repository;

import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ContextConfigurationTest;
import eu.senla.entity.Category;
import eu.senla.entity.Item;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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

        Item item = Item.builder()
                .name("itemDelById15")
                .price(new BigDecimal(345032))
                .quantity(232)
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item);
    }

    @Test
    public void createInvalidTest() {
        Item item = Item.builder()
                .price(new BigDecimal(3450)).quantity(2).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(item));
    }

    @Test
    public void getLazyAssociationsWithoutTransactionalTest() {
        fillLazyAssociationsTestDummyData();

        Long id = itemRepository.findByName("LazyAssoc").get().getId();

        Category category = itemRepository.findById(id).get().getCategory();
        Assertions.assertThrows(LazyInitializationException.class, () -> System.out.println(category));
    }

    private void fillLazyAssociationsTestDummyData() {
        Category category = Category.builder()
                .name("LazyAssoc")
                .build();
        categoryRepository.save(category);

        Item item = Item.builder()
                .name("LazyAssoc")
                .price(new BigDecimal(3450))
                .quantity(232)
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item);
    }

    @Test
    public void findByIdInTest() {
        findByIdInTestDummyData();
        Item item1 = itemRepository.findByName("findByIdIn").get();
        Item item2 = itemRepository.findByName("findByIdIn2").get();

        List<Long> ids = List.of(item1.getId(), item2.getId());
        List<Item> items = itemRepository.findByIdIn(ids);
        Assertions.assertEquals(items.get(0).getName(), item1.getName());
        Assertions.assertEquals(items.get(1).getName(), item2.getName());
    }

    private void findByIdInTestDummyData() {
        Category category = Category.builder()
                .name("findByIdIn")
                .build();
        categoryRepository.save(category);

        Item item = Item.builder()
                .name("findByIdIn")
                .price(new BigDecimal(3450))
                .quantity(232)
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item);

        Item item2 = Item.builder()
                .name("findByIdIn2")
                .price(new BigDecimal(3450))
                .quantity(232)
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item2);
    }

    @Test
    @Transactional
    public void decrementQuantityForItemsInvalidTest() {
        decrementQuantityForItemsInvalidTestDummyData();
        Item item1 = itemRepository.findByName("decQuan1invld").get();
        Item item2 = itemRepository.findByName("decQuan2invld").get();

        List<Long> ids = List.of(item1.getId(), item2.getId());
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRepository.decrementQuantityForItems(ids));
    }

    private void decrementQuantityForItemsInvalidTestDummyData() {
        Category category = Category.builder()
                .name("decQuaninvld")
                .build();
        categoryRepository.save(category);

        Item item = Item.builder()
                .name("decQuan1invld")
                .price(new BigDecimal(3450))
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item);

        Item item2 = Item.builder()
                .name("decQuan2invld")
                .price(new BigDecimal(3450))
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item2);
    }

    @Test
    @Transactional
    public void replenishItemInvalidTest() {
        replenishItemInvalidTestDummyData();
        Item item = itemRepository.findByName("replenishIteminvld").get();

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> itemRepository.replenishItem(item.getId(), -5));
    }

    private void replenishItemInvalidTestDummyData() {
        Category category = Category.builder()
                .name("replenishIteminvld")
                .build();
        categoryRepository.save(category);

        Item item = Item.builder()
                .name("replenishIteminvld")
                .price(new BigDecimal(3450))
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item);
    }
}
