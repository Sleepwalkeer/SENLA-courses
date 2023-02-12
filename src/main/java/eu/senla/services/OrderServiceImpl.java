package eu.senla.services;

import eu.senla.dao.OrderDao;
import eu.senla.dto.OrderDto;
import eu.senla.entities.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final ModelMapper modelMapper;

    public OrderDto getById(Integer id) {
        Order order = orderDao.findById(id).orElse(null);
        if (order == null) {
            return null;
        }
        return modelMapper.map(order, OrderDto.class);
    }

    public void create(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        orderDao.save(order);
    }

    public OrderDto update(Integer id, OrderDto orderDto) {
        Order order = orderDao.findById(id).orElse(null);
        if (order == null) {
            return null;
        }
        modelMapper.map(orderDto, order);
        Order updatedOrder = orderDao.update(order);
        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    public boolean deleteById(Integer id) {
        return orderDao.deleteById(id);
    }

    @Override
    public boolean delete(OrderDto orderDto) {
        return orderDao.delete(modelMapper.map(orderDto, Order.class));
    }

    public List<OrderDto> getAll() {
        List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }
}
