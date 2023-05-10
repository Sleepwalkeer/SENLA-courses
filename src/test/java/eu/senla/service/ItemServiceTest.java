package eu.senla.service;

import eu.senla.dto.categoryDto.CategoryIdDto;
import eu.senla.dto.categoryDto.ResponseCategoryDto;
import eu.senla.dto.itemDto.CreateItemDto;
import eu.senla.dto.itemDto.ItemPopularityDto;
import eu.senla.dto.itemDto.ResponseItemDto;
import eu.senla.dto.itemDto.UpdateItemDto;
import eu.senla.entity.Category;
import eu.senla.entity.Item;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.ItemOutOfStockException;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.ItemRepository;
import eu.senla.service.implementation.ItemServiceImpl;
import eu.senla.utils.converter.Converter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Converter converter;
    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTest() {
        Item item = Item.builder().id(1L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();
        CreateItemDto itemDto = CreateItemDto.builder().
                category(CategoryIdDto.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();

        when(itemRepository.save(item)).thenReturn(item);
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);
        itemService.create(itemDto);

        verify(itemRepository).save(item);
    }


    @Test
    public void getByIdTest() {
        Item item = Item.builder()
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();

        ResponseItemDto itemDto = ResponseItemDto.builder()
                .category(ResponseCategoryDto.builder().build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(modelMapper.map(item, ResponseItemDto.class)).thenReturn(itemDto);

        ResponseItemDto itemDtoRetrieved = itemService.getById(1L);

        verify(itemRepository).findById(1L);
        Assertions.assertNotNull(itemDtoRetrieved);
    }

    @Test
    public void getByNonexistentIdTest() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getById(1L));
        verify(itemRepository).findById(1L);
    }

    @Test
    public void updateTest() {
        Item item = Item.builder()
                .id(1L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();

        UpdateItemDto itemDto = UpdateItemDto.builder()
                .category(CategoryIdDto.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();

        ResponseItemDto responseItemDto = ResponseItemDto.builder()
                .category(ResponseCategoryDto.builder().name("Construction Tools").build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();

        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.existsById(1L)).thenReturn(true);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        when(modelMapper.map(item, ResponseItemDto.class)).thenReturn(responseItemDto);
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);

        ResponseItemDto itemDtoRetrieved = itemService.update(1L, itemDto);

        verify(itemRepository).existsById(1L);
        verify(itemRepository).save(item);
        Assertions.assertNotNull(itemDtoRetrieved);
    }

    @Test
    public void updateNonexistentItemTest() {
        Item item = Item.builder()
                .id(1L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();

        UpdateItemDto itemDto = UpdateItemDto.builder()
                .category(CategoryIdDto.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();

        when(itemRepository.existsById(1L)).thenReturn(false);
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);

        assertThrows(NotFoundException.class, () -> itemService.update(1L, itemDto));
        verify(itemRepository).existsById(1L);
        verify(itemRepository, times(0)).save(item);
    }

    @Test
    public void deleteByIdTest() {
        doNothing().when(itemRepository).deleteById(1L);
        when(itemRepository.existsById(1L)).thenReturn(true);
        itemService.deleteById(1L);
        verify(itemRepository).deleteById(1L);
    }

    @Test
    public void deleteByNonexistentIdTest() {
        doNothing().when(itemRepository).deleteById(1L);
        when(itemRepository.existsById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemService.deleteById(1L));
        verify(itemRepository, times(0)).deleteById(1L);
    }


    @Test
    public void getAllTest() {
        ResponseItemDto itemDto1 = ResponseItemDto.builder()
                .category(ResponseCategoryDto.builder().name("test").build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();
        List<ResponseItemDto> itemDtos = new ArrayList<>();
        itemDtos.add(itemDto1);

        Item item1 = Item.builder()
                .id(1L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();

        List<Item> items = new ArrayList<>();
        items.add(item1);

        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        Page<Item> itemPage = new PageImpl<>(items, paging, items.size());

        when(itemRepository.findAll(paging)).thenReturn(itemPage);
        when(modelMapper.map(eq(item1), eq(ResponseItemDto.class)))
                .thenReturn(itemDto1);

        List<ResponseItemDto> itemDtoList = itemService.getAll(1, 2, "id");

        Assertions.assertFalse(itemDtoList.isEmpty());
        verify(itemRepository).findAll(paging);
    }

    @Test
    public void verifyAvailabilityWhenUnavailableTest() {
        Item item = Item.builder()
                .id(1L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .available(false)
                .build();

        Item item1 = Item.builder()
                .id(2L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .available(true)
                .price(new BigDecimal(5))
                .build();

        Item item2 = Item.builder()
                .id(3L)
                .category(Category.builder().id(1L).build())
                .name("Tractor")
                .available(false)
                .price(new BigDecimal(3))
                .build();

        List<Item> items = new ArrayList<>(List.of(item, item1, item2));
        assertThrows(ItemOutOfStockException.class, () -> itemService.verifyAvailability(items));
    }

    @Test
    public void verifyAvailabilityWhenAvailableTest() {
        Item item = Item.builder()
                .id(1L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .available(true)
                .build();

        Item item1 = Item.builder()
                .id(2L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .available(true)
                .price(new BigDecimal(5))
                .build();

        Item item2 = Item.builder()
                .id(3L)
                .category(Category.builder().id(1L).build())
                .name("Tractor")
                .available(true)
                .price(new BigDecimal(3))
                .build();

        List<Item> items = new ArrayList<>(List.of(item, item1, item2));
        assertDoesNotThrow(() -> itemService.verifyAvailability(items));
    }

    @Test
    public void restockItemTest() {
        when(itemRepository.existsById(1L)).thenReturn(true);
        itemService.restockItem(1L);
        verify(itemRepository).restockItem(1L);
    }

    @Test
    public void replenishNonexistentItemTest() {
        when(itemRepository.existsById(1L)).thenReturn(false);
        Assertions.assertThrows(BadRequestException.class, () -> itemService.restockItem(1L));
        verify(itemRepository, times(0)).restockItem(1L);
    }


    @Test
    public void getItemsByPopularityTest() {
        Pageable paging = PageRequest.of(1, 2);
        List<Object[]> queryResult = new ArrayList<>();
        Object[] singleQueryResultRecord1 = {"name", 4L};
        Object[] singleQueryResultRecord2 = {"test", 2L};
        queryResult.add(singleQueryResultRecord1);
        queryResult.add(singleQueryResultRecord2);

        List<ItemPopularityDto> desiredResult = new ArrayList<>();
        desiredResult.add(new ItemPopularityDto("name", 4L));
        desiredResult.add(new ItemPopularityDto("test", 2L));


        Page<Object[]> itemPage = new PageImpl<>(queryResult, paging, queryResult.size());
        when(itemRepository.getItemsByPopularity(paging)).thenReturn(itemPage);
        when(converter.mapItemPopularityDto(itemPage)).thenReturn(desiredResult);

        List<ItemPopularityDto> retrievedResults = itemService.getItemsByPopularity(1, 2);

        verify(itemRepository).getItemsByPopularity(paging);
        Assertions.assertFalse(retrievedResults.isEmpty());

    }
}
