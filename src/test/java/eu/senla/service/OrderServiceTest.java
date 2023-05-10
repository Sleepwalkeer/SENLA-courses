package eu.senla.service;

import eu.senla.dto.accountDto.AccountIdDto;
import eu.senla.dto.orderDto.CreateOrderDto;
import eu.senla.dto.orderDto.ResponseOrderDto;
import eu.senla.dto.orderDto.UpdateOrderDto;
import eu.senla.entity.Account;
import eu.senla.entity.Category;
import eu.senla.entity.Item;
import eu.senla.entity.Order;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.AccountRepository;
import eu.senla.repository.OrderRepository;
import eu.senla.service.implementation.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private ItemService itemService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void createInvalidDateTest() {
        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .customer(AccountIdDto.builder().id(1L).build())
                .worker(AccountIdDto.builder().id(1L).build())
                .startDateTime(LocalDateTime.of(2021, 12, 3, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .build();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build())
                .startDateTime(LocalDateTime.of(2021, 12, 3, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();

        Assertions.assertThrows(BadRequestException.class, () -> orderService.create(createOrderDto));
    }

    @Test
    public void createTest() {
        List<Item> items = new ArrayList<>();
        List<Long> itemIds = new ArrayList<>(List.of(1L, 2L, 3L));

        items.add(Item.builder().id(1L).name("test1").category(Category.builder().build())
                .price(new BigDecimal(500)).discount(new BigDecimal(50)).build());
        items.add(Item.builder().id(2L).name("test2").category(Category.builder().build())
                .price(new BigDecimal(100)).build());
        items.add(Item.builder().id(3L).name("test3").category(Category.builder().discount(new BigDecimal(30)).build())
                .price(new BigDecimal(300)).build());

        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .startDateTime(LocalDateTime.of(2020, 12, 3, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2)).build();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build()).worker(Account.builder().id(1L).build())
                .items(items).startDateTime(LocalDateTime.of(2020, 12, 3, 1, 4))
                .endDateTime(LocalDateTime.of(2020, 12, 13, 1, 2))
                .build();

        Account account = Account.builder().discount(new BigDecimal(25)).build();
        Optional<Account> accountOptional = Optional.of(account);

        when(orderRepository.save(order)).thenReturn(order);
        when(modelMapper.map(createOrderDto, Order.class)).thenReturn(order);
        when(itemService.findItemsByIds(itemIds)).thenReturn(items);
        when(accountRepository.findById(order.getCustomer().getId())).thenReturn(accountOptional);
        doNothing().when(accountService).incrementCustomerDiscount(account);
        when(modelMapper.map(createOrderDto, ResponseOrderDto.class)).thenReturn(null);

        orderService.create(createOrderDto);

        verify(orderRepository).save(order);
    }

    @Test
    public void getByIdTest() {
        ResponseOrderDto orderDto = ResponseOrderDto.builder()
                .id(1L)
                .startDateTime(LocalDateTime.of(2020, 12, 10, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build())
                .startDateTime(LocalDateTime.of(2020, 12, 10, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, ResponseOrderDto.class)).thenReturn(orderDto);

        ResponseOrderDto orderDtoRetrieved = orderService.getById(1L);

        verify(orderRepository).findById(1L);
        Assertions.assertNotNull(orderDtoRetrieved);
    }


    @Test
    public void getByNonexistentIdTest() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderService.getById(1L));
        verify(orderRepository).findById(1L);
    }


    @Test
    public void updateTest() {
        List<Item> items = new ArrayList<>();

        items.add(Item.builder().id(1L).name("test1").category(Category.builder().build())
                .price(new BigDecimal(500)).discount(new BigDecimal(50)).build());
        items.add(Item.builder().id(2L).name("test2").category(Category.builder().build())
                .price(new BigDecimal(100)).build());
        items.add(Item.builder().id(3L).name("test3").category(Category.builder().discount(new BigDecimal(30)).build())
                .price(new BigDecimal(300)).build());

        UpdateOrderDto updateOrderDto = UpdateOrderDto.builder()
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2)).build();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build()).worker(Account.builder().id(1L).build())
                .items(items).startDateTime(LocalDateTime.of(2020, 12, 3, 1, 4))
                .endDateTime(LocalDateTime.of(2020, 11, 13, 1, 2))
                .totalPrice(new BigDecimal(12200)).build();

        Account account = Account.builder().discount(new BigDecimal(25)).build();
        Optional<Account> accountOptional = Optional.of(account);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(modelMapper.map(updateOrderDto, Order.class)).thenReturn(order);
        when(accountRepository.findById(order.getCustomer().getId())).thenReturn(accountOptional);
        doNothing().when(accountService).incrementCustomerDiscount(account);
        when(modelMapper.map(updateOrderDto, ResponseOrderDto.class)).thenReturn(null);

        orderService.update(1L, updateOrderDto);

        verify(orderRepository).save(order);
    }

    @Test
    public void updateNonExistentOrderTest() {
        UpdateOrderDto orderDto = UpdateOrderDto.builder()
                .endDateTime(LocalDateTime.of(2020, 12, 14, 1, 2))
                .build();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build())
                .startDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 14, 1, 2))
                .build();

        when(orderRepository.save(order)).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        when(modelMapper.map(order, UpdateOrderDto.class)).thenReturn(orderDto);
        when(modelMapper.map(orderDto, Order.class)).thenReturn(order);

        Assertions.assertThrows(NotFoundException.class, () -> orderService.update(1L, orderDto));
        verify(orderRepository).findById(1L);
        verify(orderRepository, times(0)).save(order);
    }

    @Test
    public void deleteByIdTest() {
        doNothing().when(orderRepository).deleteById(1L);
        when(orderRepository.existsById(1L)).thenReturn(true);
        orderService.deleteById(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    public void deleteByNonexistentIdTest() {
        doNothing().when(orderRepository).deleteById(1L);
        when(orderRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> orderService.deleteById(1L));
        verify(orderRepository, times(0)).deleteById(1L);
    }

    @Test
    public void getAllTest() {
        ResponseOrderDto orderDto1 = ResponseOrderDto.builder()
                .startDateTime(LocalDateTime.of(2020, 12, 2, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();

        Order order1 = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build())
                .startDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 22, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();


        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        Page<Order> orderPage = new PageImpl<>(orders, paging, orders.size());


        when(orderRepository.findAll(paging)).thenReturn(orderPage);
        when(modelMapper.map(eq(order1), eq(ResponseOrderDto.class)))
                .thenReturn(orderDto1);

        List<ResponseOrderDto> retrievedOrderDtos = orderService.getAll(1, 2, "id");

        verify(orderRepository).findAll(paging);
        Assertions.assertFalse(retrievedOrderDtos.isEmpty());
    }

    @Test
    public void calculateTotalPriceTest() {
        List<Item> items = new ArrayList<>();

        items.add(Item.builder().id(1L).name("test1").category(Category.builder().build())
                .price(new BigDecimal(500)).discount(new BigDecimal(50)).build());
        items.add(Item.builder().id(2L).name("test2").category(Category.builder().build())
                .price(new BigDecimal(100)).build());
        items.add(Item.builder().id(3L).name("test3").category(Category.builder().discount(new BigDecimal(30)).build())
                .price(new BigDecimal(300)).build());

        Order order = Order.builder()
                .customer(Account.builder().id(1L).discount(new BigDecimal(25)).build())
                .worker(Account.builder().id(1L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2020, 12, 3, 1, 4))
                .endDateTime(LocalDateTime.of(2020, 12, 13, 1, 2))
                .build();

        Assertions.assertEquals(orderService.calculateTotalPrice(order), new BigDecimal("5350.00"));
    }
}
