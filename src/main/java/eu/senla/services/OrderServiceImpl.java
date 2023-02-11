package eu.senla.services;

import eu.senla.dao.OrderDao;
import eu.senla.dto.OrderDto;
import eu.senla.entities.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final ModelMapper modelMapper;

    public List<OrderDto> getAll() {
        List<Order> orders = orderDao.findAll();
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (Order order : orders) {
            orderDtoList.add(modelMapper.map(order, OrderDto.class));
        }
        return orderDtoList;
    }

    public OrderDto getById(Integer id) {
        return modelMapper.map(orderDao.findById(id), OrderDto.class);
    }

    public void create(OrderDto orderDto) {
        orderDao.save(modelMapper.map(orderDto, Order.class));
    }

    public OrderDto update(OrderDto orderDto) {
        return modelMapper.map(orderDao.update(modelMapper.map(orderDto, Order.class)), OrderDto.class);
    }

    public void delete(OrderDto orderDto) {
        orderDao.delete(modelMapper.map(orderDto, Order.class));
    }

}
