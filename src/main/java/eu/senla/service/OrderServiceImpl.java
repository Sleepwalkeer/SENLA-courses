package eu.senla.service;

import eu.senla.repository.OrderRepository;
import eu.senla.dto.OrderDto;
import eu.senla.entity.Order;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.DatabaseAccessException;
import eu.senla.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;


    public OrderDto getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
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
        orderRepository.save(order);
    }

    public OrderDto update(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No order with ID " + id + " was found"));
        modelMapper.map(orderDto, order);
        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public void delete(OrderDto orderDto) {
        orderRepository.delete(modelMapper.map(orderDto, Order.class));
    }

    public List<OrderDto> getAll() {
        try {
            List<Order> orders = orderRepository.findAll();
            return orders.stream()
                    .map(order -> modelMapper.map(order, OrderDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseAccessException("Unable to access database");
        }
    }
}
