package eu.senla.dao;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.SecurityConfigurationTest;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.entities.Category;
import eu.senla.entities.Item;
import jakarta.persistence.PersistenceException;
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
@ContextConfiguration(classes = {Config.class, ServletConfigurationTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
public class ItemDaoTest extends ContainersEnvironment {
    @Autowired
    ItemDao itemDao;
    @Autowired
    CategoryDao categoryDao;

    @Test
    public void findyByIdTest() {
        fillFindByIdItemDummyData();
        Item item = Item.builder()
                .id(1L).category(Category.builder().id(1L).name("Construction equipment").build())
                .name("Jackhammer").price(new BigDecimal(750)).quantity(8).build();

        Optional<Item> itemFromDb = itemDao.findById(1L);
        Assertions.assertEquals(item.getId(), itemFromDb.get().getId());
    }

    private void fillFindByIdItemDummyData() {
        Category category = Category.builder().name("findItemDao").build();
        categoryDao.save(category);
        Item jackhammer = Item.builder()
                .category(Category.builder().id(1L).name("findItemDao").build())
                .name("findItemDao").price(new BigDecimal(750)).quantity(8).build();
        itemDao.save(jackhammer);
    }

    @Test
    public void updateTest() {
        fillUpdateItemDummyData();
        Optional<Item> itemOptional = itemDao.findById(1L);
        Item item = itemOptional.get();

        item.setName("Ferrari");
        Item itemFromDb = itemDao.save(item);
        Assertions.assertEquals(item.getName(), itemFromDb.getName());
    }

    private void fillUpdateItemDummyData() {
        Category category = Category.builder().name("updItemDao").build();
        categoryDao.save(category);
        Item jackhammer = Item.builder()
                .category(Category.builder().id(1L).name("updItemDao").build())
                .name("updItemDao").price(new BigDecimal(750)).quantity(8).build();
        itemDao.save(jackhammer);
    }

    @Test
    public void deleteByIdTest() {
        fillDeleteItemByIdDummyData();
        itemDao.deleteById(5L);
        Assertions.assertFalse(itemDao.findById(5L).isPresent());
    }
    private void fillDeleteItemByIdDummyData() {
        Category category = Category.builder().name("deleteByIdDaoItem").build();
        categoryDao.save(category);

        Category category1 = Category.builder().name("deleteById2DaoItem").build();
        categoryDao.save(category1);

        Item jackhammer = Item.builder()
                .category(Category.builder().id(1L).build())
                .name("itemDelById11").price(new BigDecimal(750)).quantity(8).build();
        itemDao.save(jackhammer);
        Item angleGrinder = Item.builder()
                .category(Category.builder().id(1L).name("Construction equipment").build())
                .name("itemDelById12").price(new BigDecimal(600)).quantity(15).build();
        itemDao.save(angleGrinder);

        Item twoBedApp = Item.builder()
                .category(Category.builder().id(1L).name("Real estate").build())
                .name("itemDelById13").price(new BigDecimal(3450)).quantity(2).build();
        itemDao.save(twoBedApp);

        Item lamborghini = Item.builder()
                .category(Category.builder().id(2L).name("Vehicles").build())
                .name("itemDelById14").price(new BigDecimal(6300)).quantity(1).build();
        itemDao.save(lamborghini);

        Item itemForDeletion = Item.builder()
                .category(Category.builder().id(1L).name("Real estate").build())
                .name("itemDelById15").price(new BigDecimal(345032)).quantity(232).build();
        itemDao.save(itemForDeletion);
    }

    @Test
    public void addInvalidDataTest() {
        Item item = Item.builder()
                .category(Category.builder().id(3L).name("Real estate").build()).price(new BigDecimal(3450)).quantity(2).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemDao.save(item));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertFalse(itemDao.findById(-4L).isPresent());
    }
}
