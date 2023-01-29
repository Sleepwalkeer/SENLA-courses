package eu.senla.services;

import eu.senla.annotation.Transaction;
import eu.senla.dao.OrderDao;
import eu.senla.dto.OrderDto;
import eu.senla.entities.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderDao orderDao, ModelMapper modelMapper) {
        this.orderDao = orderDao;
        this.modelMapper = modelMapper;
    }

    @Transaction
    public OrderDto transactionTest() {
        Order order3 = new Order();
        order3.setId(3);
        return modelMapper.map(orderDao.getById(order3), OrderDto.class);
    }

    public List<OrderDto> getAll() {
        List<Order> orders = orderDao.getAll();
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (Order order : orders) {
            orderDtoList.add(modelMapper.map(order, OrderDto.class));
        }
        return orderDtoList;
    }

    public OrderDto getById(OrderDto orderDto) {
        return modelMapper.map(orderDao.getById(modelMapper.map(orderDto, Order.class)), OrderDto.class);
    }

    public OrderDto create(OrderDto orderDto) {
        return modelMapper.map(orderDao.create(modelMapper.map(orderDto, Order.class)), OrderDto.class);
    }

    public OrderDto update(OrderDto orderDto) {
        return modelMapper.map(orderDao.update(modelMapper.map(orderDto, Order.class)), OrderDto.class);
    }

    public void delete(OrderDto orderDto) {
        orderDao.delete(modelMapper.map(orderDto, Order.class));
    }

}
