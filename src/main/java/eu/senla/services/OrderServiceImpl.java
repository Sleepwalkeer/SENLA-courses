package eu.senla.services;

import eu.senla.dao.OrderDao;
import eu.senla.dto.OrderDto;
import eu.senla.entities.Account;
import eu.senla.entities.Order;
import eu.senla.exceptions.BadRequestException;
import eu.senla.exceptions.DatabaseAccessException;
import eu.senla.exceptions.NotFoundException;
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
        Order order = orderDao.findById(id).orElseThrow(() ->
                new NotFoundException("No order with ID " + id + " was found"));
        return modelMapper.map(order, OrderDto.class);
    }

    public void create(OrderDto orderDto) {
        if (orderDto.getCustomer() == null || orderDto.getWorker() == null) {
            throw new BadRequestException("Customer and worker are required");
        }
        if (orderDto.getStartDateTime().compareTo(orderDto.getEndDateTime()) >= 0) {
            throw new BadRequestException("Start DateTime cannot be later than end DateTime");
        }
        Order order = modelMapper.map(orderDto, Order.class);
        orderDao.save(order);
    }

    public OrderDto update(Integer id, OrderDto orderDto) {
        Order order = orderDao.findById(id).orElseThrow(() ->
                new NotFoundException("No order with ID " + id + " was found"));
        modelMapper.map(orderDto, order);
        Order updatedOrder = orderDao.update(order);
        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    public boolean deleteById(Integer id) {
        if (orderDao.deleteById(id)){
            return true;
        }
        else throw new NotFoundException("No order with ID " + id + " was found");
    }

    @Override
    public boolean delete(OrderDto orderDto) {
        if (orderDao.delete(modelMapper.map(orderDto, Order.class))){
            return true;
        }
        else throw new NotFoundException("No such order was found");
    }

    public List<OrderDto> getAll() {
        try {
            List<Order> orders = orderDao.findAll();
            return orders.stream()
                    .map(order -> modelMapper.map(order, OrderDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseAccessException("Unable to access database");
        }
    }
}
