package eu.senla.dao;

import eu.senla.Config;
import eu.senla.entities.*;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ContextConfiguration(classes = {Config.class})
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemDaoTest {
    @Autowired
    ItemDao itemDao;
    @Autowired
    CategoryDao categoryDao;

    @BeforeAll
    public void setUp() {
        fillDatabaseWithDummyData();
    }

    @Test
    public void findyByIdEagerTest() {
        Item item = Item.builder()
                .id(1).category(Category.builder().id(1).name("Construction equipment").build())
                .name("Jackhammer").price(new BigDecimal(750)).quantity(8).build();

        Item itemFromDb = itemDao.findByIdEager(1);
        Assertions.assertEquals(item, itemFromDb);
    }

    @Test
    public void updateTest() {
        Item item = itemDao.findById(4);


        item.setName("Ferrari");
        Item itemFromDb = itemDao.update(item);
        Assertions.assertEquals(item.getName(), itemFromDb.getName());
    }

    @Test
    public void deleteByIdTest() {
        itemDao.deleteById(5);
        Assertions.assertNull(itemDao.findById(5));
    }

    @Test
    public void addInvalidDataTest() {
        Item item = Item.builder()
                .category(Category.builder().id(3).name("Real estate").build())
                .name("2-bedroom app").price(new BigDecimal(3450)).quantity(2).build();
        Assertions.assertThrows(PersistenceException.class, () -> itemDao.save(item));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertNull(itemDao.findById(-4));
    }

    @Test
    @Transactional
    public void getLazyAssociations() {
        Item item = itemDao.findById(2);
        Category category = item.getCategory();
        System.out.println(category);
    }

    private void fillDatabaseWithDummyData() {
        Category category = Category.builder().name("Luxury").build();
        categoryDao.save(category);

        Category category1 = Category.builder().name("Automobiles").build();
        categoryDao.save(category1);

        Category category2 = Category.builder().name("Property").build();
        categoryDao.save(category2);

        Item jackhammer = Item.builder()
                .category(Category.builder().id(1).name("Construction equipment").build())
                .name("Jackhammer").price(new BigDecimal(750)).quantity(8).build();
        itemDao.save(jackhammer);
        Item angleGrinder = Item.builder()
                .category(Category.builder().id(1).name("Construction equipment").build())
                .name("Angle grinder").price(new BigDecimal(600)).quantity(15).build();
        itemDao.save(angleGrinder);

        Item twoBedApp = Item.builder()
                .category(Category.builder().id(3).name("Real estate").build())
                .name("2-bedroom app").price(new BigDecimal(3450)).quantity(2).build();
        itemDao.save(twoBedApp);

        Item lamborghini = Item.builder()
                .category(Category.builder().id(2).name("Vehicles").build())
                .name("Lamborghini").price(new BigDecimal(6300)).quantity(1).build();
        itemDao.save(lamborghini);

        Item itemForDeletion = Item.builder()
                .category(Category.builder().id(3).name("Real estate").build())
                .name("test").price(new BigDecimal(345032)).quantity(232).build();
        itemDao.save(itemForDeletion);
    }
}
