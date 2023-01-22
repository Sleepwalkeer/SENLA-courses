package eu.senla.services;

import eu.senla.dao.OrderDao;
import eu.senla.dto.OrderDto;
import eu.senla.entities.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderService {
    private final OrderDao orderDao;
    private final ModelMapper modelMapper;

    public OrderService(OrderDao orderDao, ModelMapper modelMapper) {
        this.orderDao = orderDao;
        this.modelMapper = modelMapper;
    }

    public List<OrderDto> getAll() {
        List<OrderDto> orderDtos = new ArrayList<>();
        List<Order> orders = orderDao.getAll();

        for (Order order : orders) {
            orderDtos.add(fromEntityToDto(order));
        }
        return orderDtos;
    }

    public OrderDto getById(OrderDto orderDto) {
        return fromEntityToDto(orderDao.getById(fromDtoToEntity(orderDto)));
    }

    public OrderDto update(OrderDto orderDto, BigDecimal newTotalPrice) {
        return fromEntityToDto(orderDao.update(fromDtoToEntity(orderDto), newTotalPrice));
    }

    public OrderDto create(OrderDto orderDto) {
        return fromEntityToDto(orderDao.create(fromDtoToEntity(orderDto)));
    }

    public void delete(OrderDto orderDto) {
        orderDao.delete(fromDtoToEntity(orderDto));
    }

    private Order fromDtoToEntity(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }

    private OrderDto fromEntityToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
