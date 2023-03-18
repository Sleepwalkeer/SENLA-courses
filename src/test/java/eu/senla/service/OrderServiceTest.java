package eu.senla.service;

import eu.senla.dto.accountDto.AccountDto;
import eu.senla.dto.accountDto.AccountIdDto;
import eu.senla.dto.orderDto.CreateOrderDto;
import eu.senla.dto.orderDto.OrderDto;
import eu.senla.dto.orderDto.ResponseOrderDto;
import eu.senla.dto.orderDto.UpdateOrderDto;
import eu.senla.entity.Account;
import eu.senla.entity.Order;
import eu.senla.exception.NotFoundException;
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
import java.sql.Timestamp;
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

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    //TODO ЭТОТ ТЕСТ НАДО ДЕЛАТ НОРМАЛЬНЫМ ОН ПО БИЗНЕСУ С РАССЧЕТОМ ДЕНЯХ
    public void createTest() {
        CreateOrderDto createOrderDto = CreateOrderDto.builder().customer(AccountIdDto.builder().id(1L).build())
                .worker(AccountIdDto.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).build();
        Order order = Order.builder().customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();

        when(orderRepository.save(order)).thenReturn(order);
        when(modelMapper.map(createOrderDto, Order.class)).thenReturn(order);

        orderService.create(createOrderDto);

        verify(orderRepository).save(order);
    }

    @Test
    public void getByIdTest() {
        ResponseOrderDto orderDto = ResponseOrderDto.builder()
                .id(1L)
                .startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L))
                .totalPrice(new BigDecimal(12200))
                .build();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build())
                .startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L))
                .totalPrice(new BigDecimal(12200))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(order));
        when(modelMapper.map(order, ResponseOrderDto.class)).thenReturn(orderDto);

        ResponseOrderDto orderDtoRetrieved = orderService.getById(1L);

        verify(orderRepository).findById(1L);
        Assertions.assertNotNull(orderDto);
        Assertions.assertNotNull(orderDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderService.getById(1L));
        verify(orderRepository).findById(1L);
    }


//    @Test
//    //TODO ЭТОТ ТЕСТ ТОЖЕ НАДО ХОРОШИМ ДЕЛАТЬ
//    public void updateTest() {
//        OrderDto orderDto = OrderDto.builder().customer(AccountDto.builder().id(1L).build())
//                .worker(AccountDto.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
//                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
//        Order order = Order.builder().customer(Account.builder().id(1L).build())
//                .worker(Account.builder().id(1L).build()).startDateTime(new Timestamp(1665778114323L))
//                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
//
//        when(orderRepository.save(order)).thenReturn(order);
//        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(order));
//        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);
//        when(modelMapper.map(orderDto, Order.class)).thenReturn(order);
//
//        OrderDto orderDtoRetrieved = orderService.update(1L, orderDto);
//
//        verify(orderRepository).findById(1L);
//        verify(orderRepository).save(order);
//        Assertions.assertEquals(orderDto, orderDtoRetrieved);
//    }

    @Test
    public void updateNonExistentOrderTest() {
        UpdateOrderDto orderDto = UpdateOrderDto.builder()
                .customer(AccountIdDto.builder().id(1L).build())
                .startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L))
                .build();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build())
                .startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L))
                .build();

        when(orderRepository.save(order)).thenReturn(order);
        when(orderRepository.existsById(1L)).thenReturn(false);
        when(modelMapper.map(order, UpdateOrderDto.class)).thenReturn(orderDto);
        when(modelMapper.map(orderDto, Order.class)).thenReturn(order);

        Assertions.assertThrows(NotFoundException.class, () -> orderService.update(1L, orderDto));
        verify(orderRepository).existsById(1L);
    }

    @Test
    public void deleteByIdTest() {
        doNothing().when(orderRepository).deleteById(1L);
        when(orderRepository.existsById(1L)).thenReturn(true);
        orderService.deleteById(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    public void getAllTest() {
        OrderDto orderDto1 = OrderDto.builder()
                .customer(AccountDto.builder().id(1L).build())
                .worker(AccountDto.builder().id(1L).build())
                .startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L))
                .totalPrice(new BigDecimal(12200))
                .build();

        OrderDto orderDto2 = OrderDto.builder()
                .customer(AccountDto.builder().id(1L).build())
                .worker(AccountDto.builder().id(2L).build())
                .startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L))
                .totalPrice(new BigDecimal(11600))
                .build();

        Order order1 = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(1L).build())
                .startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L))
                .totalPrice(new BigDecimal(12200))
                .build();

        Order order2 = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L))
                .totalPrice(new BigDecimal(11600))
                .build();

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        Page<Order> orderPage = new PageImpl<>(orders, paging, orders.size());


        when(orderRepository.findAll(paging)).thenReturn(orderPage);
        when(modelMapper.map(eq(order1), eq(OrderDto.class)))
                .thenReturn(orderDto1);
        when(modelMapper.map(eq(order2), eq(OrderDto.class)))
                .thenReturn(orderDto2);

        List<ResponseOrderDto> retrievedOrderDtos = orderService.getAll(1, 2, "id");

        verify(orderRepository).findAll(paging);
    }
}
