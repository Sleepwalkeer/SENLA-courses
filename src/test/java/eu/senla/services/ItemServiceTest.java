package eu.senla.services;

import eu.senla.dao.CategoryDao;
import eu.senla.dao.ItemDao;
import eu.senla.dto.CategoryDto;
import eu.senla.dto.ItemDto;
import eu.senla.entities.Category;
import eu.senla.entities.Item;
import eu.senla.exceptions.BadRequestException;
import eu.senla.exceptions.DatabaseAccessException;
import eu.senla.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ItemServiceTest {
    @Mock
    private ItemDao itemDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTest() {
        Item item = Item.builder().id(1).category(Category.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();
        ItemDto itemDto = ItemDto.builder().id(1).category(CategoryDto.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();

        doNothing().when(itemDao).save(item);
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);

        itemService.create(itemDto);

        verify(itemDao).save(item);

    }

    @Test
    public void createWithInvalidDataTest() {
        ItemDto itemDto = ItemDto.builder().build();
        Assertions.assertThrows(BadRequestException.class, () -> itemService.create(itemDto));
    }

    @Test
    public void getByIdTest() {
        Item item = Item.builder().id(1).category(Category.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();
        ItemDto itemDto = ItemDto.builder().id(1).category(CategoryDto.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();

        when(itemDao.findById(1)).thenReturn(Optional.ofNullable(item));
        when(modelMapper.map(item, ItemDto.class)).thenReturn(itemDto);

        ItemDto itemDtoRetrieved = itemService.getById(1);

        verify(itemDao).findById(1);
        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(itemDto, itemDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(itemDao.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.getById(1));
        verify(itemDao).findById(1);
    }

    @Test
    public void updateTest() {
        Item item = Item.builder().id(1).category(Category.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();
        ItemDto itemDto = ItemDto.builder().id(1).category(CategoryDto.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();

        when(itemDao.update(item)).thenReturn(item);
        when(itemDao.findById(1)).thenReturn(Optional.ofNullable(item));
        when(modelMapper.map(item, ItemDto.class)).thenReturn(itemDto);
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);

        ItemDto itemDtoRetrieved = itemService.update(1, itemDto);


        verify(itemDao).findById(1);
        verify(itemDao).update(item);
        Assertions.assertEquals(itemDto, itemDtoRetrieved);
    }

    @Test
    public void updateNonExistentItemTest() {
        Item item = Item.builder().id(1).category(Category.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();
        ItemDto itemDto = ItemDto.builder().id(1).category(CategoryDto.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();

        when(itemDao.update(item)).thenReturn(item);
        when(itemDao.findById(1)).thenReturn(Optional.empty());
        when(modelMapper.map(item, ItemDto.class)).thenReturn(itemDto);
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);

        Assertions.assertThrows(NotFoundException.class, () -> itemService.update(1, itemDto));
        verify(itemDao).findById(1);
    }


    @Test
    public void deleteTest() {
        Item item = Item.builder().id(1).category(Category.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();
        ItemDto itemDto = ItemDto.builder().id(1).category(CategoryDto.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();

        when(itemDao.delete(item)).thenReturn(true);
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);

        Assertions.assertTrue(itemService.delete(itemDto));
        verify(itemDao).delete(item);
    }

    @Test
    public void deleteNonExistentItemTest() {
        Item item = Item.builder().id(1).category(Category.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();
        ItemDto itemDto = ItemDto.builder().id(1).category(CategoryDto.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();

        when(itemDao.delete(item)).thenReturn(false);
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);

        Assertions.assertFalse(itemService.delete(itemDto));
        verify(itemDao).delete(item);
    }

    @Test
    public void deleteByIdTest() {
        when(itemDao.deleteById(1)).thenReturn(true);

        Assertions.assertTrue(itemService.deleteById(1));
        verify(itemDao).deleteById(1);
    }

    @Test
    public void deleteByNonExistentIdTest() {
        when(itemDao.deleteById(1)).thenReturn(false);

        Assertions.assertFalse(itemService.deleteById(1));
        verify(itemDao).deleteById(1);
    }

    @Test
    public void getAllTest() {
        ItemDto itemDto1 = ItemDto.builder().id(1).category(CategoryDto.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();
        ItemDto itemDto2 = ItemDto.builder().id(2).category(CategoryDto.builder().id(2).name("Luxury").build())
                .name("Porsche Cayenne").price(new BigDecimal(500)).quantity(1).build();
        List<ItemDto> itemDtos = new ArrayList<>();
        itemDtos.add(itemDto1);
        itemDtos.add(itemDto2);

        Item item1 = Item.builder().id(1).category(Category.builder().id(1).name("Construction Tools").build())
                .name("JackHammer").price(new BigDecimal(5)).quantity(3).build();
        Item item2 = Item.builder().id(2).category(Category.builder().id(2).name("Luxury").build())
                .name("Porsche Cayenne").price(new BigDecimal(500)).quantity(1).build();
        List<Item> categories = new ArrayList<>();
        categories.add(item1);
        categories.add(item2);

        when(itemDao.findAll()).thenReturn(categories);
        when(modelMapper.map(eq(item1), eq(ItemDto.class)))
                .thenReturn(itemDto1);
        when(modelMapper.map(eq(item2), eq(ItemDto.class)))
                .thenReturn(itemDto2);

        List<ItemDto> retrievedItemDtos = itemService.getAll();

        verify(itemDao).findAll();
        Assertions.assertIterableEquals(itemDtos, retrievedItemDtos);
    }

    @Test
    public void getAllWithDatabaseAccessExceptionTest() {
        when(itemDao.findAll()).thenThrow(new RuntimeException());
        Assertions.assertThrows(DatabaseAccessException.class, () -> itemService.getAll());
        verify(itemDao).findAll();
    }
}
