package eu.senla.services;

import eu.senla.dto.OrderDto;
import eu.senla.entities.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAll();

    OrderDto getById(OrderDto accountDto);

    OrderDto create(OrderDto accountDto);

    OrderDto update(OrderDto accountDto);

    void delete(OrderDto accountDto);

    OrderDto transactionTest();
}
