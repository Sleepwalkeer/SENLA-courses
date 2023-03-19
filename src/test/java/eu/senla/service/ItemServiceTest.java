package eu.senla.service;

import eu.senla.dto.categoryDto.CategoryDto;
import eu.senla.dto.categoryDto.CategoryIdDto;
import eu.senla.dto.categoryDto.ResponseCategoryDto;
import eu.senla.dto.itemDto.CreateItemDto;
import eu.senla.dto.itemDto.ItemDto;
import eu.senla.dto.itemDto.ResponseItemDto;
import eu.senla.dto.itemDto.UpdateItemDto;
import eu.senla.entity.Category;
import eu.senla.entity.Item;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.ItemRepository;
import eu.senla.service.implementation.ItemServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
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
        Item item = Item.builder().id(1L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .quantity(3)
                .build();
        CreateItemDto itemDto = CreateItemDto.builder().
                category(CategoryIdDto.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .quantity(3)
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

        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(modelMapper.map(item, ResponseItemDto.class)).thenReturn(itemDto);

        ResponseItemDto itemDtoRetrieved = itemService.getById(1L);

        verify(itemRepository).findById(1L);
        Assertions.assertNotNull(itemDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.getById(1L));
        verify(itemRepository).findById(1L);
    }

    @Test
    public void updateTest() {
        Item item = Item.builder()
                .id(1L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .quantity(3)
                .build();

        UpdateItemDto itemDto = UpdateItemDto.builder()
                .category(CategoryIdDto.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .quantity(3)
                .build();

        ResponseItemDto responseItemDto = ResponseItemDto.builder()
                .category(ResponseCategoryDto.builder().name("Construction Tools").build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .build();

        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.existsById(1L)).thenReturn(true);
        when(modelMapper.map(item, ResponseItemDto.class)).thenReturn(responseItemDto);
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);

        ResponseItemDto itemDtoRetrieved = itemService.update(1L, itemDto);

        verify(itemRepository).existsById(1L);
        verify(itemRepository).save(item);
        Assertions.assertNotNull(itemDtoRetrieved);
    }

    @Test
    public void updateNonExistentItemTest() {
        Item item = Item.builder()
                .id(1L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .quantity(3)
                .build();

        UpdateItemDto itemDto = UpdateItemDto.builder()
                .category(CategoryIdDto.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .quantity(3)
                .build();

        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.existsById(1L)).thenReturn(false);
        when(modelMapper.map(item, UpdateItemDto.class)).thenReturn(itemDto);
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);

        Assertions.assertThrows(NotFoundException.class, () -> itemService.update(1L, itemDto));
        verify(itemRepository).existsById(1L);
    }

    @Test
    public void deleteByIdTest() {
        doNothing().when(itemRepository).deleteById(1L);
        when(itemRepository.existsById(1L)).thenReturn(true);
        itemService.deleteById(1L);
        verify(itemRepository).deleteById(1L);
    }


    @Test
    public void getAllTest() {
        ItemDto itemDto1 = ItemDto.builder()
                .id(1L)
                .category(CategoryDto.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .quantity(3)
                .build();

        ItemDto itemDto2 = ItemDto.builder()
                .id(2L)
                .category(CategoryDto.builder().id(2L).build())
                .name("Porsche Cayenne")
                .price(new BigDecimal(500))
                .quantity(1)
                .build();

        List<ItemDto> itemDtos = new ArrayList<>();
        itemDtos.add(itemDto1);
        itemDtos.add(itemDto2);

        Item item1 = Item.builder()
                .id(1L)
                .category(Category.builder().id(1L).build())
                .name("JackHammer")
                .price(new BigDecimal(5))
                .quantity(3)
                .build();

        Item item2 = Item.builder()
                .id(2L)
                .category(Category.builder().id(2L).build())
                .name("Porsche Cayenne")
                .price(new BigDecimal(500))
                .quantity(1)
                .build();
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        Page<Item> itemPage = new PageImpl<>(items, paging, items.size());

        when(itemRepository.findAll(paging)).thenReturn(itemPage);
        when(modelMapper.map(eq(item1), eq(ItemDto.class)))
                .thenReturn(itemDto1);
        when(modelMapper.map(eq(item2), eq(ItemDto.class)))
                .thenReturn(itemDto2);

       //List<ResponseItemDto> retrievedItemDtos = itemService.getAll(1, 2, "id");

        verify(itemRepository).findAll(paging);
    }
}
