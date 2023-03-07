package eu.senla.services;

import eu.senla.dao.OrderDao;
import eu.senla.dto.AccountDto;
import eu.senla.dto.OrderDto;
import eu.senla.entities.Account;
import eu.senla.entities.Order;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


public class OrderServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTest() {
        OrderDto orderDto = OrderDto.builder().customer(AccountDto.builder().id(1L).build())
                .worker(AccountDto.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        Order order = Order.builder().customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();

        when(orderDao.save(order)).thenReturn(order);
        when(modelMapper.map(orderDto, Order.class)).thenReturn(order);

        orderService.create(orderDto);

        verify(orderDao).save(order);
    }

    @Test
    public void createWithInvalidDataTest() {
        OrderDto orderDto = OrderDto.builder().customer(AccountDto.builder().id(1L).build())
                .worker(AccountDto.builder().id(1L).build()).startDateTime(new Timestamp(1765778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        Assertions.assertThrows(BadRequestException.class, () -> orderService.create(orderDto));
    }

    @Test
    public void getByIdTest() {
        OrderDto orderDto = OrderDto.builder().customer(AccountDto.builder().id(1L).build())
                .worker(AccountDto.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        Order order = Order.builder().customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();

        when(orderDao.findById(1L)).thenReturn(Optional.ofNullable(order));
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

        OrderDto orderDtoRetrieved = orderService.getById(1L);

        verify(orderDao).findById(1L);
        Assertions.assertNotNull(orderDto);
        Assertions.assertEquals(orderDto, orderDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(orderDao.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderService.getById(1L));
        verify(orderDao).findById(1L);
    }

    @Test
    public void updateTest() {
        OrderDto orderDto = OrderDto.builder().customer(AccountDto.builder().id(1L).build())
                .worker(AccountDto.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        Order order = Order.builder().customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();

        when(orderDao.save(order)).thenReturn(order);
        when(orderDao.findById(1L)).thenReturn(Optional.ofNullable(order));
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);
        when(modelMapper.map(orderDto, Order.class)).thenReturn(order);

        OrderDto orderDtoRetrieved = orderService.update(1L, orderDto);


        verify(orderDao).findById(1L);
        verify(orderDao).save(order);
        Assertions.assertEquals(orderDto, orderDtoRetrieved);
    }

    @Test
    public void updateNonExistentOrderTest() {
        OrderDto orderDto = OrderDto.builder().customer(AccountDto.builder().id(1L).build())
                .worker(AccountDto.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        Order order = Order.builder().customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();

        when(orderDao.save(order)).thenReturn(order);
        when(orderDao.findById(1L)).thenReturn(Optional.empty());
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);
        when(modelMapper.map(orderDto, Order.class)).thenReturn(order);

        Assertions.assertThrows(NotFoundException.class, () -> orderService.update(1L, orderDto));
        verify(orderDao).findById(1L);
    }


    @Test
    public void deleteTest() {
        OrderDto orderDto = OrderDto.builder().customer(AccountDto.builder().id(1L).build())
                .worker(AccountDto.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        Order order = Order.builder().customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();

        doNothing().when(orderDao).delete(order);
        when(modelMapper.map(orderDto, Order.class)).thenReturn(order);
        orderService.delete(orderDto);

        verify(orderDao).delete(order);
    }


    @Test
    public void deleteByIdTest() {
        doNothing().when(orderDao).deleteById(1L);
        orderService.deleteById(1L);

        verify(orderDao).deleteById(1L);
    }

    @Test
    public void getAllTest() {
        OrderDto orderDto1 = OrderDto.builder().customer(AccountDto.builder().id(1L).build())
                .worker(AccountDto.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();

        OrderDto orderDto2 = OrderDto.builder().customer(AccountDto.builder().id(1L).build())
                .worker(AccountDto.builder().id(2L).build()).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(11600)).build();
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(orderDto1);
        orderDtos.add(orderDto2);

        Order order1 = Order.builder().customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();

        Order order2 = Order.builder().customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build()).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(11600)).build();
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        when(orderDao.findAll()).thenReturn(orders);
        when(modelMapper.map(eq(order1), eq(OrderDto.class)))
                .thenReturn(orderDto1);
        when(modelMapper.map(eq(order2), eq(OrderDto.class)))
                .thenReturn(orderDto2);

        List<OrderDto> retrievedOrderDtos = orderService.getAll();

        verify(orderDao).findAll();
        Assertions.assertIterableEquals(orderDtos, retrievedOrderDtos);
    }

    @Test
    public void getAllWithDatabaseAccessExceptionTest() {
        when(orderDao.findAll()).thenThrow(new RuntimeException());
        Assertions.assertThrows(DatabaseAccessException.class, () -> orderService.getAll());
        verify(orderDao).findAll();
    }
}
